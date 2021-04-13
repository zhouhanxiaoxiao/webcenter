package com.cibr.logincenter.dao.logincenter;

import com.cibr.logincenter.entity.CibrSysWebsite;
import com.cibr.logincenter.entity.CibrSysWebsiteExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CibrSysWebsiteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int countByExample(CibrSysWebsiteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int deleteByExample(CibrSysWebsiteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int insert(CibrSysWebsite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int insertSelective(CibrSysWebsite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    List<CibrSysWebsite> selectByExample(CibrSysWebsiteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    CibrSysWebsite selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CibrSysWebsite record, @Param("example") CibrSysWebsiteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CibrSysWebsite record, @Param("example") CibrSysWebsiteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CibrSysWebsite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_website
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CibrSysWebsite record);
}