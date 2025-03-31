package com.rajib.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rajib.exception.OurException;

@Service
public class LocalStorageService {

	private final String storageDirectory = "C:\\Users\\HP\\Pictures\\image";
	
	public String saveImageToLocal(MultipartFile photo) {
		try {
			if(!photo.isEmpty()) {
				Path dirPath = Paths.get(storageDirectory);
				if(!Files.exists(dirPath)) {
					Files.createDirectories(dirPath);
				}
				String originalFilename = photo.getOriginalFilename();
				String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
				Path filePath = dirPath.resolve(uniqueFilename);
				
				Files.copy(photo.getInputStream(), filePath);
				return filePath.toString();
			}else {
				throw new OurException("Image file is Empty.");
			}
		}catch(Exception e) {
			throw new OurException("Unable to save image locally: "+e.getMessage());
		}
	}
}
