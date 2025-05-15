package com.cementerio.cemeteryProject_management.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("El archivo está vacío o no se proporcionó");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new IllegalArgumentException("No se pudo obtener el nombre del archivo");
            }

            // Extraer la extensión y el nombre base para el public_id
            String extension = "";
            String baseName = originalFilename;
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex + 1).toLowerCase();
                baseName = originalFilename.substring(0, dotIndex);
            }
            String publicId = baseName.replace(" ", "_").replaceAll("[^a-zA-Z0-9-_]", "");
            String contentType = file.getContentType();

            byte[] fileBytes = file.getBytes();
            System.out.println("Tamaño del archivo leído: " + fileBytes.length + " bytes");
            System.out.println("Subiendo archivo: " + originalFilename + ", contentType: " + contentType);

            String resourceType;
            String forcedContentType;
            String forcedFormat = null;
            if (extension.equals("pdf")) {
                resourceType = "raw";
                forcedContentType = "application/pdf";
                forcedFormat = "pdf"; // Forzar el formato como pdf
                System.out.println("Forzando resource_type: raw, content_type: application/pdf, format: pdf para PDF");
            } else if (contentType != null && contentType.startsWith("image/")) {
                resourceType = "image";
                forcedContentType = contentType;
                forcedFormat = extension; // Por ejemplo, "jpg", "png"
                System.out.println("Usando resource_type: image, content_type: " + contentType + ", format: " + forcedFormat + " para imagen");
            } else {
                resourceType = "raw";
                forcedContentType = "application/octet-stream";
                System.out.println("Tipo de archivo desconocido, usando resource_type: raw, content_type: application/octet-stream");
            }

            Map<String, Object> options = new HashMap<>();
            options.put("public_id", publicId);
            options.put("resource_type", resourceType);
            options.put("overwrite", true);
            options.put("use_filename", true);
            if (forcedContentType != null) {
                options.put("content_type", forcedContentType);
            }
            if (forcedFormat != null) {
                options.put("format", forcedFormat); // Forzar el formato
            }

            Map uploadResult = cloudinary.uploader().upload(fileBytes, options);
            String urlArchivo = uploadResult.get("url").toString();
            System.out.println("Resultado de Cloudinary - URL: " + urlArchivo);
            System.out.println("Resultado de Cloudinary - Detectado por Cloudinary: " + uploadResult.get("resource_type") + ", " + uploadResult.get("format"));
            return urlArchivo;
        } catch (Exception e) {
            System.err.println("Error al subir el archivo a Cloudinary: " + e.getMessage());
            throw new IOException("Error al subir el archivo a Cloudinary: " + e.getMessage(), e);
        }
    }
}