package life.kaoyan.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import life.kaoyan.community.exception.CustomizeErrorCode;
import life.kaoyan.community.exception.CustomizeException;
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
    private String bucketName = "zhkaoyan";;

    public String upload(InputStream fileStream, String mimeType,String fileName){
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length>1){
            generatedFileName= UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey,privateKey);
            ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)

                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
                    if (response!=null &&response.getRetCode()==0){
                        String url = UfileClient.object(objectAuthorization, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, 24*60*60)
                                /**
                                 * 使用Content-Disposition: attachment，并且默认文件名为KeyName
                                 */
//                    .withAttachment()
                                /**
                                 * 使用Content-Disposition: attachment，并且配置文件名
                                 */
//                    .withAttachment("filename")
                                .createUrl();
                                return url;
                    }else {
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
                    }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
