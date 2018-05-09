
<form name="addarticle" id="addarticle"
      action="article"
      method="post">
    <table style="width: 500px; height: auto;">
        <tr>
            <td>标题：</td>
            <td><input type="text" value=""
                       name="title" id="title"
                       style="width: 100%; height: 28px; padding-left: 5px; margin-top: 3px;" />
                <!-- 有 id就是回帖 -->


                <input type="hidden" id="postrootaction" name="action" value="addz" />
                <input type="hidden" id="postrootid" name="rootid" value="" />
                <input type="hidden" id="zid" name="zid" value="" />
                <input type="hidden" name="userid" id="userid" value="<#if user??>${user.userid}</#if>" />

            </td>
        </tr>
        <tr>
            <td valign="top">内容：</td>
            <td><textarea id="content" name="content" id="content" rows="10"
                          cols="30"></textarea>


            </td>
        </tr>


    </table>


</form>