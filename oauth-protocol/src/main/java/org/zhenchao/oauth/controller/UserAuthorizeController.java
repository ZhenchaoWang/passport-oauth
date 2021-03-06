package org.zhenchao.oauth.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.RequestPath;
import org.zhenchao.oauth.common.cipher.AESCoder;
import org.zhenchao.oauth.common.util.ScopeUtils;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.pojo.ResultInfo;
import org.zhenchao.oauth.service.AuthorizeRelationService;
import org.zhenchao.oauth.token.constant.TokenConstant;
import org.zhenchao.oauth.util.JsonView;
import org.zhenchao.oauth.util.ResponseUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * User Authorize
 *
 * @author zhenchao.wang 2017-02-14 17:32
 * @version 1.0.0
 */
@Controller
@RequestMapping(RequestPath.PATH_ROOT_OAUTH)
public class UserAuthorizeController {

    private static final Logger log = LoggerFactory.getLogger(UserAuthorizeController.class);

    @Resource
    private AuthorizeRelationService authorizeRelationService;

    private AESCoder aesCoder = new AESCoder();

    /**
     * user authorize on app
     *
     * @return
     */
    @RequestMapping(value = RequestPath.PATH_OAUTH_USER_AUTHORIZE, method = {POST})
    public ModelAndView userAuthorize(HttpServletResponse response,
                                      @RequestParam("user_id") long userId,
                                      @RequestParam("client_id") long clientId,
                                      @RequestParam("scope") String scope,
                                      @RequestParam(value = "state", required = false) String state,
                                      @RequestParam("callback") String callback) {

        log.debug("Do user authorize, userId[{}], clientId[{}]", userId, clientId);

        ModelAndView mav = new ModelAndView();
        if (userId < 0 || StringUtils.isBlank(callback)) {
            log.error("User authorize request params error, clientId[{}], userId[{}], callback[{}]", clientId, userId, callback);
            return ResponseUtils.buildLoginResponse(callback);
        }

        // 添加用户授权关系
        AuthorizeRelation relation = new AuthorizeRelation();
        relation.setUserId(userId);
        relation.setAppId(clientId);
        relation.setScope(scope);
        relation.setScopeSign(ScopeUtils.getScopeSign(scope));
        relation.setCreateTime(new Date());
        relation.setCreateTime(relation.getCreateTime());
        relation.setTokenKey(Base64.encodeBase64String(aesCoder.initKey()));  // 随机生成key
        relation.setRefreshTokenKey(Base64.encodeBase64String(aesCoder.initKey()));  // 随机生成刷新令牌key
        relation.setRefreshTokenExpirationTime(TokenConstant.DEFAULT_REFRESH_TOKEN_VALIDITY);  // 设置刷新令牌有效期
        if (authorizeRelationService.replaceAuthorizeRelation(relation)) {
            // 更新用户授权关系成功
            log.info("Replace authorize relation success, userId[{}], appId[{}], scope[{}]", userId, clientId, scope);
            try {
                mav.setViewName(String.format("redirect:%s&skip_confirm=true", URLDecoder.decode(callback, "UTF-8")));
                return mav;
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        log.info("Replace authorize relation error, userId[{}], appId[{}], scope[{}]", userId, clientId, scope);
        return JsonView.render(new ResultInfo(ErrorCode.LOCAL_SERVICE_ERROR, state), response, false);
    }

}
