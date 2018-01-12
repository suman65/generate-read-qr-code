package com.mystrings.qr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mystrings.qr.services.QRCodeService;

@RestController
public class QRCodeController 
{

	@Autowired private QRCodeService service;
	
	@RequestMapping(value="generateQRCode",method = RequestMethod.GET)
    public void generateQRCode(@RequestParam(value = "qrCodeText", required = false) String qrCodeText)
    {
		service.generateQRCodeImage(qrCodeText);
    }
	
	@RequestMapping(value="readQRCode",method = RequestMethod.GET)
    public String readQRCode()
    {
		return service.readQRCodeFromImage();
    }
	@RequestMapping(value="readQRCodeFromByteArray",method = RequestMethod.GET)
    public String readQRCode(@RequestParam(value = "qrCodeText", required = false) String qrCodeText)
    {
		return service.readQRCodeFromByteArray(qrCodeText);
    }
}
