package com.scm.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile>{
	
	public static final long MAX_FILE_SIZE = 1024*1024*2;//2 MB
	
	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		if(file==null || file.isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
			return false;
		}
		
		if(file.getSize()>MAX_FILE_SIZE) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("File size should be less than 2 MB").addConstraintViolation();
			return false;
		}
		
		//TODO:how to get image resolution and check
		/*try {
			BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
			if()
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return true;
	}
	
}
