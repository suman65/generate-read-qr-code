package com.mystrings.qr.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mystrings.qr.utils.EncryptDecrypt;

import lombok.extern.slf4j.Slf4j;

public interface QRCodeService 
{
	public  void generateQRCodeImage(String qrCodeText);
	public  byte[] getQRCodeImageByteArray(String qrCodeText);
	public String readQRCodeFromImage();
	public String readQRCodeFromByteArray(String text);
	

	@Service
	@Slf4j
	public class impl implements QRCodeService
	{
		private static final String QR_CODE_IMAGE_PATH = "C:/Suman/QRCode.png";
		
		@Autowired private EncryptDecrypt encryptDecrypt;
		@Override
		public byte[] getQRCodeImageByteArray(String text) 
		{
	        try 
	        {
				QRCodeWriter qrCodeWriter = new QRCodeWriter();
				BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);

				ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
				MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
				byte[] pngData = pngOutputStream.toByteArray();
				return pngData;
			} 
	        catch (Exception e) 
	        {
	        	log.info("Exception while generating QR code :: ",e.getCause(),e);
	        	return null;
			}
		}
		@Override
		public void generateQRCodeImage(String qrCodeText) 
		{
			try
			{
				
				String encrpyted = encryptDecrypt.encrypt(qrCodeText);
				log.info("Encrpted string :: "+encrpyted);
				QRCodeWriter qrCodeWriter = new QRCodeWriter();
		        BitMatrix bitMatrix = qrCodeWriter.encode(encrpyted, BarcodeFormat.QR_CODE, 350, 350);

		        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
		        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
			}
			catch (Exception e)
			{
				log.info("Exception while generating QR code :: ",e.getCause(),e);
			}
		}

		@Override
		public String readQRCodeFromImage() 
		{
	        try 
	        {
	        	File file = new File(QR_CODE_IMAGE_PATH);
				BufferedImage bufferedImage = ImageIO.read(file);
		        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	            Result result = new MultiFormatReader().decode(bitmap);
	            log.info("decrypted string sas :: "+result.getText());
	            String decrypted = encryptDecrypt.decrypt(result.getText());
				log.info("decrypted string :: "+decrypted);
	            return decrypted;
	        } 
	        catch (Exception e) 
	        {
	            System.out.println("There is no QR code in the image");
	            return null;
	        }
		}
		@Override
		public String readQRCodeFromByteArray(String text) 
		{
			try 
	        {
				byte[] qrBytes = getQRCodeImageByteArray(text);
				InputStream targetStream = new ByteArrayInputStream(qrBytes);
				BufferedImage bufferedImage = ImageIO.read(targetStream);
		        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	            Result result = new MultiFormatReader().decode(bitmap);
	            return result.getText();
	        } 
	        catch (Exception e) 
	        {
	            System.out.println("There is no QR code in the image");
	            return null;
	        }
		}
	}
}
