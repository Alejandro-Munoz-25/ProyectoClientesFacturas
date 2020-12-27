package com.springboot.app.models.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.springboot.app.models.entity.FirebaseCredential;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final static String UPLOADS_FOLDER = "uploads";
	private final Environment environment;

	private StorageOptions storageOptions;
	private String bucketName;
	private String projectId;

	public UploadFileServiceImpl(Environment environment) {
		this.environment = environment;
	}

	@PostConstruct
	private void initializeFirebase() throws Exception {
		bucketName = environment.getRequiredProperty("FIREBASE_BUCKET_NAME");
		projectId = environment.getRequiredProperty("FIREBASE_PROJECT_ID");

		InputStream firebaseCredential = createFirebaseCredential();
		this.storageOptions = StorageOptions.newBuilder().setProjectId(projectId)
				.setCredentials(GoogleCredentials.fromStream(firebaseCredential)).build();

	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		File fileP = convertMultiPartToFile(file);
		Path filePath = fileP.toPath();
		String objectName = UUID.randomUUID().toString().concat("_").concat(file.getOriginalFilename());

		BlobId blobId = BlobId.of(bucketName, "uploads/" + objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Storage storage = storageOptions.getService();
		@SuppressWarnings("unused")
		Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
		return objectName;
	}

	@Override
	public boolean delete(String filename) {
		BlobId blobId = BlobId.of(bucketName, "uploads/" + filename);
		if (blobId != null) {
			Storage storage = storageOptions.getService();
			if (storage.delete(bucketName, "uploads/" + filename)) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}

	private InputStream createFirebaseCredential() throws Exception {
		FirebaseCredential firebaseCredential = new FirebaseCredential();
		// private key
		String privateKey = environment.getRequiredProperty("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");
		firebaseCredential.setType(environment.getRequiredProperty("FIREBASE_TYPE"));
		firebaseCredential.setProject_id(projectId);
		firebaseCredential.setPrivate_key_id("FIREBASE_PRIVATE_KEY_ID");
		firebaseCredential.setPrivate_key(privateKey);
		firebaseCredential.setClient_email(environment.getRequiredProperty("FIREBASE_CLIENT_EMAIL"));
		firebaseCredential.setClient_id(environment.getRequiredProperty("FIREBASE_CLIENT_ID"));
		firebaseCredential.setAuth_uri(environment.getRequiredProperty("FIREBASE_AUTH_URI"));
		firebaseCredential.setToken_uri(environment.getRequiredProperty("FIREBASE_TOKEN_URI"));
		firebaseCredential.setAuth_provider_x509_cert_url(
				environment.getRequiredProperty("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
		firebaseCredential.setClient_x509_cert_url(environment.getRequiredProperty("FIREBASE_CLIENT_X509_CERT_URL"));

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(firebaseCredential);

		InputStream targetStream = new ByteArrayInputStream(jsonString.getBytes());
		return targetStream;
	}
}
