package com.spm.teacherhelperv2.controller;

import com.alibaba.fastjson.JSONObject;
import com.spm.teacherhelperv2.entity.MenuDO;
import com.spm.teacherhelperv2.entity.Tree;
import com.spm.teacherhelperv2.manager.RespondResult;
import com.spm.teacherhelperv2.service.MenuService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  MenuController前端控制器
 * @author zhangjr
 * @since 2020-05-16 17:28:25
 */

@RestController
@RequestMapping("${global.version}/menus")
@Api(tags = "s_menu表操作API")	
public class MenuController {
    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    @Qualifier("MenuService")
    private MenuService menuService;

    /**
     * 获取满足某些条件的全部数据列表
     * @param fieldValue 查询条件值
     * @param fieldName 查询条件值属性名
     * @param page 页数
     * @param limit 每页限制条数
     * @return menuDO实体类数据列表
     */
    @GetMapping("/")
//    @RequiresPermissions({ "menu:list" })
    @ApiOperation(value = "获取满足某些条件的全部数据列", httpMethod = "GET", notes = "用于通过指定条件,查询s_menu表对应所用数据")
    @ApiImplicitParams({ @ApiImplicitParam(name = "page", value = "请求页码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "limit", value = "每页数据条数", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fieldValue", value = "查询条件值", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fieldName", value = "查询条件值属性名", paramType = "query", dataType = "String")})
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult listMenuByOther(@RequestParam(value = "page", required = false) String page,
                                         @RequestParam(value = "limit", required = false) String limit,
                                         @RequestParam(value = "fieldValue", required = false) String fieldValue,
                                         @RequestParam(value = "fieldName", required = false) String fieldName) {
        logger.info("receive:[page:"+page+"--limit:"+limit+"--fieldValue:"+fieldValue+"--fieldName:"+fieldName+"]");
        try {
            List<MenuDO> menus = menuService.listMenuByOther(fieldValue, fieldName, page, limit);
            Integer total=menuService.countAllMenus();
            Map data=new HashMap();
            data.put("data",menus);
            data.put("total",total);
            return RespondResult.success("查找成功",data);
        }catch (Exception e){
            return RespondResult.error("查找失败");
        }


    }


    /**
     * 根据ID查找数据
     * @return menuDO实体类数据
     */
    @GetMapping("/{menuId}")
//    @RequiresPermissions({"menu:list" })
    @ApiOperation(value = "根据ID查找数据",httpMethod = "GET",notes = "用于通过menuId，查询s_menu表中对应的一条数据")
    @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "path", dataType = "String")
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult getMenuById(@ApiParam(value = "menuId", required = true)@PathVariable("menuId")String menuId) {
        logger.info("receive:[menuId:"+menuId+"]");
        try {
            MenuDO menuDO = menuService.getMenuById(Long.valueOf(menuId));
            return RespondResult.success("查找成功",menuDO);
        }catch (Exception e){
            return RespondResult.error("查找失败");
        }
    }

    /**
     * 根据其他查找数据
     * @param fieldValue 查询条件值
     * @param fieldName 查询条件值属性名
     * @return menuDO实体类数据
     */
    @GetMapping("/menu")
//    @RequiresPermissions({ "menu:list" })
    @ApiOperation(value = "根据其他查找数据", httpMethod = "GET", notes = "用于通过指定字段，查询uuuec_user表中对应的一条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fieldValue", value = "查询条件值", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fieldName", value = "查询条件值属性名", paramType = "query", dataType = "String")
    })
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult getMenuByOther(@RequestParam(value = "fieldValue", required = false) String fieldValue
            ,@RequestParam(value = "fieldName", required = false) String fieldName) {
        logger.info("receive:[fieldValue:"+fieldValue+"--fieldName:"+fieldName+"]");
        try {
            MenuDO menuDO = menuService.getMenuByOther(fieldValue,fieldName);
            return RespondResult.success("查找成功",menuDO);
        }catch (Exception e){
            return RespondResult.error("查找失败");
        }

    }

    /**
     * 添加数据
     * @return menuDO实体类数据
     */
    @PostMapping("/")
//    @RequiresPermissions({ "menu:list", "menu:add"})
    @ApiOperation(value = "添加数据",httpMethod = "POST",notes = "用于在s_menu表中插入对应的一条数据，为异步方法，结果会回调到异步地址中\n;"
            + "MenuDO实体类数据{\n"
            + "变量名：\"parentId\",类型：Long,注释：上级菜单ID\n,"
            + "变量名：\"menuName\",类型：String,注释：菜单/按钮名称\n,"
            + "变量名：\"url\",类型：String,注释：菜单URL\n,"
            + "变量名：\"perms\",类型：String,注释：权限标识\n,"
            + "变量名：\"icon\",类型：String,注释：图标\n,"
            + "变量名：\"type\",类型：String,注释：类型 0菜单 1按钮\n,"
            + "变量名：\"orderNum\",类型：Long,注释：排序\n,"
            + "}\n")
    @ApiImplicitParam(name = "data", value = "menuDO实体类数据", paramType = "body", dataType = "String")
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult insertMenu(@RequestBody(required=true)String data) {
        logger.info("receive:[data:"+data+"]");
        try {
            MenuDO menuDO=JSONObject.parseObject(data, MenuDO.class);
            menuService.insertMenu(menuDO);
            return RespondResult.success("添加数据成功",menuDO);
        }catch (Exception e){
            return RespondResult.error("添加数据失败");
        }

    }


    /**
     * 更新数据
     * @param data menuDO实体类数据
     * @return menuDO实体类数据
     */
    @PutMapping("/{menuId}")
//    @RequiresPermissions({"menu:list", "menu:update"})
    @ApiOperation(value = "更新数据",httpMethod = "PUT",notes = "用于更新s_menu表中对应的一条数据，为异步方法，结果会回调到异步地址中\n"
            + "MenuDO实体类数据{\n"
            + "变量名：\"parentId\",类型：Long,注释：上级菜单ID\n,"
            + "变量名：\"menuName\",类型：String,注释：菜单/按钮名称\n,"
            + "变量名：\"url\",类型：String,注释：菜单URL\n,"
            + "变量名：\"perms\",类型：String,注释：权限标识\n,"
            + "变量名：\"icon\",类型：String,注释：图标\n,"
            + "变量名：\"type\",类型：String,注释：类型 0菜单 1按钮\n,"
            + "变量名：\"orderNum\",类型：Long,注释：排序\n,"
            + "}\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "data", value = "menuDO实体类数据", paramType = "body", dataType = "String")
    })
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult updateMenuDO(@RequestBody(required=true)String data) {
        logger.info("receive:[data:"+data+"]");
        try {
            MenuDO menuDO=JSONObject.parseObject(data, MenuDO.class);
            menuDO = menuService.updateMenu(menuDO);
            return RespondResult.success("更新数据成功",menuDO);
        }catch (Exception e){
            return RespondResult.error("更新数据失败");
        }

    }

    /**
     * 更新部分数据
     * @param menuId menuId
     * @param data menuDO部分信息
     * @return GetResult<Boolean>(suatus:状态码,msg:消息,data:处理结果)
     */
    @PatchMapping("/{menuId}")
//    @RequiresPermissions({ "menu:list", "menu:update"})
    @ApiOperation(value = "更新部分数据",httpMethod = "PATCH",notes = "用于更新s_menu表中对应的一条数据，为异步方法，结果会回调到异步地址中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "data", value = "menuDO实体类数据", paramType = "body", dataType = "String")
    })
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult updateMenuForField(@ApiParam(value = "menuId", required = true)@PathVariable("menuId")String menuId,
                                            @RequestBody(required=true) String data) {
        logger.info("receive:[menuId:"+menuId+"-:deleteata:"+data+"]");
        try {
            MenuDO menuDO = menuService.updateMenuField(data, Long.valueOf(menuId));
            return RespondResult.success("更新部分数据成功",menuDO);
        }catch (Exception e){
            return RespondResult.error("更新部分数据失败");
        }
    }

    /**
     * 根据Id删除数据
     * @param menuDOId menuDOId
     * @return GetResult<Boolean>(suatus:状态码,msg:消息,data:处理结果)
     */
    @DeleteMapping("/{menuId}")
//    @RequiresPermissions({"menu:list", "menu:delete"})
    @ApiOperation(value = "根据Id删除数据",httpMethod = "DELETE",notes = "用于通过menuDOId，删除s_menu表中对应的一条数据，为异步方法，结果会回调到异步地址中")
    @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "path", dataType = "String")
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult deleteMenuDOById(@ApiParam(value = "menuIdListString", required = true)@PathVariable("menuId")String menuId) {
        Boolean flag = null;
        logger.info("receive:[menuId:"+menuId+"]");
        try {
            flag = menuService.deleteMenuById(menuId);
            return RespondResult.success("删除成功",flag);
        }catch (Exception e){
            return RespondResult.error("删除失败");
        }

    }

	@GetMapping("/getUserMenus")
	@ApiOperation(value = "根据username找到菜单",httpMethod = "GET",notes = "用于通过username找到菜单")
	@ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
			@ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
			@ApiResponse(code = 555, message = "请求超时，请重试") })
	public RespondResult getUserMenus(@RequestParam String username){
		try {
			Tree<MenuDO> tree=this.menuService.findUserMenu(username);
			return RespondResult.success("获取成功",tree);

		}catch (Exception e){
			return RespondResult.error("获取用户菜单失败");
		}
	}

    @GetMapping("/getAllMenus")
    @ApiOperation(value = "找到所有菜单",httpMethod = "GET",notes = "找到菜单")
    @ApiResponses({ @ApiResponse(code = 551, message = "第三方平台错误"), @ApiResponse(code = 552, message = "本平台错误"),
            @ApiResponse(code = 553, message = "权限不够"), @ApiResponse(code = 554, message = "请求数据有误"),
            @ApiResponse(code = 555, message = "请求超时，请重试") })
    public RespondResult getAllMenus(){
        try {
            Tree<MenuDO> tree=this.menuService.findAllMenus();
            return RespondResult.success("获取成功",tree);

        }catch (Exception e){
            return RespondResult.error("获取菜单失败");
        }
    }
}