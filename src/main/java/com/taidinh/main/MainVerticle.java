package com.taidinh.main;

import com.taidinh.main.utils.RSAUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Set;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/encrypt").handler(routingContext -> {
            Set<FileUpload> fileUploadSet = routingContext.fileUploads();
            Buffer fileUploaded = null;
            for (FileUpload fileUpload : fileUploadSet) {
                if (fileUpload.name().equals("publicKey")) {
                    fileUploaded = routingContext.vertx().fileSystem().readFileBlocking(fileUpload.uploadedFileName());
                }
            }
            System.out.print(new String(fileUploaded.getBytes()));
            HttpServerRequest request = routingContext.request();
            String data = request.getFormAttribute("data");
            System.out.print(request.formAttributes());
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end(getRSAEncryptedMessage(fileUploaded.getBytes(), data));
        });
        vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
            if (http.succeeded()) {
                startFuture.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startFuture.fail(http.cause());
            }
        });
    }


    private String getRSAEncryptedMessage(byte[] publicKey, String data) {
        try {
            return RSAUtils.encrypt(data, publicKey);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "Có lỗi rồi kìa liên hệ Tài Đinh";
    }

}
