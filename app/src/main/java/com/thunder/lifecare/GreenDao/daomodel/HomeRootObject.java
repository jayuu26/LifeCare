package com.thunder.lifecare.GreenDao.daomodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity( // If you have more than one schema, you can tell greenDAO
        // to which schema an entity belongs (pick any string as a name).
//        schema = "life-care-schema",
        generateGettersSetters = true)

public class HomeRootObject {
    @Id(autoincrement = true)
    private Long id;

    private String category;
    private String name;
    private String type;


    @ToMany(referencedJoinProperty = "home_sub_id")
    @OrderBy("id ASC")
    private List<HomeCategory> homeCategory;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1342366984)
    private transient HomeRootObjectDao myDao;

    @Generated(hash = 1498409995)
    public HomeRootObject(Long id, String category, String name, String type) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.type = type;
    }

    @Generated(hash = 967888458)
    public HomeRootObject() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }




    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1223220598)
    public List<HomeCategory> getHomeCategory() {
        if (homeCategory == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HomeCategoryDao targetDao = daoSession.getHomeCategoryDao();
            List<HomeCategory> homeCategoryNew = targetDao
                    ._queryHomeRootObject_HomeCategory(id);
            synchronized (this) {
                if (homeCategory == null) {
                    homeCategory = homeCategoryNew;
                }
            }
        }
        return homeCategory;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1517192338)
    public synchronized void resetHomeCategory() {
        homeCategory = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1371140742)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHomeRootObjectDao() : null;
    }
}