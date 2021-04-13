package com.cibr.logincenter.dao.logincenter;

import com.cibr.logincenter.entity.CibrSysFile;
import com.cibr.logincenter.entity.CibrSysFileExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CibrSysFileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int countByExample(CibrSysFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int deleteByExample(CibrSysFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int insert(CibrSysFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int insertSelective(CibrSysFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    List<CibrSysFile> selectByExample(CibrSysFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    CibrSysFile selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CibrSysFile record, @Param("example") CibrSysFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CibrSysFile record, @Param("example") CibrSysFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CibrSysFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_file
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CibrSysFile record);
}