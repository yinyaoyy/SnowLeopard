<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- <scirpt src="src/main/webapp/static/jquery/jquery-1.8.3.js"></scirpt> -->
<div class="modal" ng-controller="KisBpmAssignmentPopupCtrl">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="close()">&times;</button>
                <h2 translate>PROPERTY.ASSIGNMENT.TITLE</h2>
            </div>
            <div class="modal-body">
            
            	<div class="row row-no-gutter">
            		<div class="form-group">
            			<label for="assigneeField">{{'PROPERTY.ASSIGNMENT.ASSIGNEE' | translate}}</label>
            		<%-- <sys:treeselect id="oaLeaveRecordIds" name="oaLeaveRecordIds"
						value="${oaLeave.oaLeaveRecordIds}"
						labelName="oaLeaveRecordNames"
						labelValue="${oaLeave.oaLeaveRecordNames}" title="用户"
						url="/sys/office/treeDataAll?type=3"
						cssClass="form-control required valid" notAllowSelectParent="true"
						checked="true" 
						addonText="选择"
						
						/> --%>
            			<input type="text" id="assigneeField" class="form-control" ng-model="assignment.assignee" placeholder="{{'PROPERTY.ASSIGNMENT.ASSIGNEE_PLACEHOLDER' | translate}}" /> 
            		</div>
            	</div>
            	
                <div class="row row-no-gutter">
                    <div class="form-group">
                    	<label for="userField">{{'PROPERTY.ASSIGNMENT.CANDIDATE_USERS' | translate}}</label>
                        <div ng-repeat="candidateUser in assignment.candidateUsers">
            	            <input id="userField" class="form-control" type="text" ng-model="candidateUser.value" />
            	            <i class="glyphicon glyphicon-minus clickable-property" ng-click="removeCandidateUserValue($index)"></i>
            	            <i ng-if="$index == (assignment.candidateUsers.length - 1)" class="glyphicon glyphicon-plus clickable-property" ng-click="addCandidateUserValue($index)"></i>
                        </div>
                   	</div>
            
                    <div class="form-group">
                    	<label for="groupField">{{'PROPERTY.ASSIGNMENT.CANDIDATE_GROUPS' | translate}}</label>
                        <div ng-repeat="candidateGroup in assignment.candidateGroups">
            	          	<input id="groupField" class="form-control" type="text" ng-model="candidateGroup.value" />
            	          	<i class="glyphicon glyphicon-minus clickable-property" ng-click="removeCandidateGroupValue($index)"></i>
            	          	<i ng-if="$index == (assignment.candidateGroups.length - 1)" class="glyphicon glyphicon-plus clickable-property" ng-click="addCandidateGroupValue($index)"></i>
                        </div>
                    </div>
                </div>
            
            </div>
            <div class="modal-footer">
                <button ng-click="close()" class="btn btn-primary" translate>ACTION.CANCEL</button>
                <button ng-click="save()" class="btn btn-primary" translate>ACTION.SAVE</button>
            </div>
        </div>
    </div>
</div>