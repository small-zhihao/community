package life.kaoyan.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    public String upload(InputStream fileStream, String mimeType,String fileName){
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length>1){
            generatedFileName= UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            return null;
        }

        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey,privateKey);
            ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket("zhkaoyan")

                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
        } catch (UfileClientException e) {
            e.printStackTrace();
            return null;
        } catch (UfileServerException e) {
            e.printStackTrace();
            return null;
        }
        return generatedFileName;
    }
}
