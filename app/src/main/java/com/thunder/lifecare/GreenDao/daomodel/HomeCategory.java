package com.thunder.lifecare.GreenDao.daomodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(generateGettersSetters = true)
public class HomeCategory
{
  @Id(autoincrement = true)
  private Long id;
  
  private String name;
  private String iconUrl;
  private String itemUrl;

  private long home_sub_id;

    @Generated(hash = 1256595275)
    public HomeCategory(Long id, String name, String iconUrl, String itemUrl,
            long home_sub_id) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.itemUrl = itemUrl;
        this.home_sub_id = home_sub_id;
    }
@Generated(hash = 893952841)
  public HomeCategory() {
  }
  public Long getId() {
      return this.id;
  }
  public void setId(Long id) {
      this.id = id;
  }
  public String getName() {
      return this.name;
  }
  public void setName(String name) {
      this.name = name;
  }
  public String getIconUrl() {
      return this.iconUrl;
  }
  public void setIconUrl(String iconUrl) {
      this.iconUrl = iconUrl;
  }
  public String getItemUrl() {
      return this.itemUrl;
  }
  public void setItemUrl(String itemUrl) {
      this.itemUrl = itemUrl;
  }
public long getHome_sub_id() {
    return this.home_sub_id;
}
public void setHome_sub_id(long home_sub_id) {
    this.home_sub_id = home_sub_id;
}

}