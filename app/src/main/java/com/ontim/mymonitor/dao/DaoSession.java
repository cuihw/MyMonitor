package com.ontim.mymonitor.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ontim.mymonitor.data.UserInfo;

import com.ontim.mymonitor.dao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userInfoDaoConfig;

    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        userInfoDaoConfig.clearIdentityScope();
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
