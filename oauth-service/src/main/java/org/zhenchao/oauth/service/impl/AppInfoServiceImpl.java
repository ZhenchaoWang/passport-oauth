package org.zhenchao.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.oauth.dao.OAuthAppInfoMapper;
import org.zhenchao.oauth.model.OAuthAppInfo;
import org.zhenchao.oauth.service.AppInfoService;

import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link AppInfoService} 实现类
 *
 * @author zhenchao.wang 2017-01-20 17:47
 * @version 1.0.0
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {

    private static final Logger log = LoggerFactory.getLogger(AppInfoServiceImpl.class);

    @Resource
    private OAuthAppInfoMapper appInfoMapper;

    @Override
    public Optional<OAuthAppInfo> getAppInfo(long clientId) {
        log.debug("Get oauth app info by id[{}].", clientId);
        if (clientId < 0) {
            log.error("Client id [{}] error");
            return Optional.empty();
        }
        return Optional.ofNullable(appInfoMapper.selectByPrimaryKey(clientId));
    }

}
