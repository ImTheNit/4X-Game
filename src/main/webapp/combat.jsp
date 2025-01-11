<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<script>
		
        
        
        
        function replaceFightContent(newHtml) {
        	const jsonObject = JSON.parse(newHtml);
            const newHtmlContent = jsonObject.fightSummary;
            const contentDiv = document.getElementById('fight');
            contentDiv.innerHTML = newHtmlContent;
            
        }
</script>

</html>