package shenzhen.teamway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import shenzhen.teamway.service.CameraOnvifServiceImp;

import java.io.IOException;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 14:32
 **/
@Component
public class RestErrorHandler extends DefaultResponseErrorHandler {
    Logger log = LoggerFactory.getLogger(RestErrorHandler.class);


    @Override
    protected boolean hasError(HttpStatus statusCode) {
        return false;

    }

    @Override
    public void handleError(ClientHttpResponse response) {
        log.info("出异常了");

    }
}