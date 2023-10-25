package softuni.WebFinderserver.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Component
public class CloudUtil {

    private final Cloudinary cloudinary;

    public CloudUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile multipartFile) throws IOException {

        String imageId = UUID.randomUUID().toString();

        File tempFile = new File(imageId);

        Files.write(tempFile.toPath(), multipartFile.getBytes());
        cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "public_id",imageId,
                "overwrite",true,
                "resource_type","image",
                "type","upload"
        ));

        Files.delete(tempFile.toPath());

        return imageId;
    }

    public String takeUrl(String fileName) {
        Transformation transformation = new Transformation().width(300).height(300).crop("fill");
        return cloudinary.url().transformation(transformation).format("jpg").generate(fileName);
    }

}
