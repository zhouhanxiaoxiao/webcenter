package com.cibr.logincenter.dao.user;

import com.cibr.logincenter.entity.CibrSysRole;
import com.cibr.logincenter.entity.CibrSysRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CibrSysRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int countByExample(CibrSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int deleteByExample(CibrSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int insert(CibrSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int insertSelective(CibrSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    List<CibrSysRole> selectByExample(CibrSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    CibrSysRole selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CibrSysRole record, @Param("example") CibrSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CibrSysRole record, @Param("example") CibrSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CibrSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CibrSysRole record);
}