package fr.insalyon.creatis.vip.core.server.business.utils;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class Permissions {

    public static void checkLevel(User user, UserLevel... authorizedLevels) throws BusinessException {
        for (UserLevel level: authorizedLevels) {
            if (level.equals(user.getLevel())) {
                return;
            }
        }
        throw new BusinessException("You do not have the right to do that!");
    }
}
