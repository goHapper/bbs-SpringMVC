  <div class="pagination">
    <ul>
        <li>
            
                <a href="article?action=queryall&page=1">首页</a>
			
        </li>
        <li>
            <#if pb??>
			<#if (pb.curPage>1)>
					<a href="article?action=queryall&page=${pb.curPage-1}">前一页</a>
            </#if>

		</li>
    	<li>
			<#list 1..pb.maxPage as i>
				<a href="article?action=queryall&page=${i}">${i}</a>
            </#list>
        </li>

    	<li>
        <#if (pb.curPage<pb.maxPage)>

            <a href="article?action=queryall&page=${pb.curPage+1}">下一页</a>
        </#if>
			
        </li>

    	<li>


                <a href="article?action=queryall&page=${pb.maxPage}">尾页</a>
			

        </li>
            </#if>

    </ul>
</div>