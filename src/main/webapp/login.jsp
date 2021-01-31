<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>系统入口</title>
    <!--引入样式主题-->
    <link rel="stylesheet" href="/statics/easyui/themes/bootstrap/easyui.css">
    <!--引入图标样式-->
    <link rel="stylesheet" href="/statics/easyui/themes/icon.css">
    <!--引入jQuery EasyUI 插件 -->
    <script src="/statics/jquery.min.js"></script>
    <script src="/statics/easyui/jquery.easyui.min.js"></script>
    <script src="/statics/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script src="/statics/jquery.extd.js"></script>
    <script src="/statics/jquery.cookie.js"></script>

    <script>
        $(function () {
            //（1）、用户登陆页面 --初始化会话框
            $("#user_login_dialog").dialog({
                title: "用户登陆",
                iconCls: "icon-logo",
                top: 120,
                width: 300,
                modal: true,//模式化窗口，窗口后面的 按钮不允许 操作
                draggable: false,
                closable: false,
                closed: false,
                cache: false,
                //引入 登录的页面元素
                href: "commons/login/loginForm.html",
                //登录框底部
                buttons: [{
                    text: "登陆",
                    iconCls: "icon-ok",
                    plain: true,//

                    //----提交 请求到后台
                    handler: function () {
                        $("#user_login_form").form("submit", {
                            url: "${pageContext.request.contextPath}/formUserManager/userLogin",
                            //---表单的验证 返回true进行表单的提交
                            onSubmit: function () {
                                if (!$("#user_login_form").form("validate")) {
                                    $.messager.alert("表单提示", "请检查您的表单输入")
                                    return false
                                }
                                $.messager.progress();
                                //---开始生成cookies inputVector 传给后台。
                                var vectors = $("#user_login_form").data("inputVector");
                                $.cookie("inputVector", vectors.join(","))

                                return true
                            },

                            success: function (data) {
                                $.messager.progress('close');	// 如果提交成功则隐藏进度条
                                if (data) {
                                    var error = eval('(' + data + ')');  //json 装字符串
                                    console.log(error)
                                    $.messager.alert("登陆出错！", "错误信息：" + error.erroCode + ",提示:" + error.message)
                                } else {
                                    window.location.href = "/"
                                }
                            }
                        })
                    }
                }, {
                    text: '重置',
                    iconCls: "icon-redo",
                    plain: true,
                    handler: function () {
                        //拿到表单对象 通过方法 重置表单数据
                        $("#user_login_form").form("reset")
                    }
                }],
                toolbar: [{
                    text: '用户注册',
                    plain: true,
                    handler: function () {
                        $("#user_login_dialog").dialog("close")
                        $("#user_register_dialog").dialog("open")
                    }
                }],

                // 调用 输入时间检测
                onLoad: function () {
                    $("#user_login_form").actions({
                        vectorName: "inputVector"
                    })
                }
            })

            //（2）、用户注册页面
            $("#user_register_dialog").dialog({
                title: "用户注册",
                iconCls: "icon-logo",
                top: 120,
                width: 300,
                modal: true,
                draggable: false,
                closable: false,
                closed: true,
                cache: false,
                href: "commons/login/registerForm.html",
                buttons: [{
                    text: "注册",
                    iconCls: "icon-ok",
                    plain: true,
                    handler: function () {
                        $("#user_rigister_form").form("submit", {
                            url: "formUserManager/registerUser",
                            onSubmit: function () {
                                if (!$("#user_rigister_form").form("validate")) {
                                    $.messager.alert("表单提示", "请检查您的表单输入")
                                    return false
                                }
                                $.messager.progress();
                                return true
                            },
                            success: function (data) {
                                $.messager.progress('close');	// 如果提交成功则隐藏进度条
                                if (data) {
                                    var error = eval('(' + data + ')');
                                    console.log(error)
                                    $.messager.alert("注册出错！", "错误信息：" + error.erroCode + ",提示:" + error.message)
                                } else {
                                    window.location.href = "/"
                                }
                            }
                        })
                    }
                }, {
                    text: '重置',
                    iconCls: "icon-redo",
                    plain: true,
                    handler: function () {
                        $("#user_rigister_form").form("reset")
                    }
                }],
                toolbar: [{
                    text: '用户登陆',
                    plain: true,
                    handler: function () {
                        $("#user_register_dialog").dialog("close")
                        $("#user_login_dialog").dialog("open")
                    }
                }]
            })

        })
    </script>
</head>


<body style="background-color: #3f3f3f">
<!--登陆Dialog-->
<div id="user_login_dialog">
</div>


<div id="user_register_dialog">
</div>
</body>