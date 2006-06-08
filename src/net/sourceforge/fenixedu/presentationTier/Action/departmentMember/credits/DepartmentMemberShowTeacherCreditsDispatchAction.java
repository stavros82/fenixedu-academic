package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowTeacherCreditsDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

public class DepartmentMemberShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm teacherCreditsForm = (DynaActionForm) form;
        ExecutionPeriod executionPeriod = rootDomainObject
                .readExecutionPeriodByOID((Integer) teacherCreditsForm.get("executionPeriodId"));

        Teacher requestedTeacher = rootDomainObject.readTeacherByOID((Integer) teacherCreditsForm
                .get("teacherId"));
        
        IUserView userView = SessionUtils.getUserView(request);
        Teacher loggedTeacher = userView.getPerson().getTeacher();

        if (requestedTeacher == null || loggedTeacher != requestedTeacher) {            
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("message.invalid.teacher"));
            saveMessages(request, actionMessages);
            return mapping.findForward("teacher-not-found");
        }

        getAllTeacherCredits(request, executionPeriod, requestedTeacher);
        return mapping.findForward("show-teacher-credits");
    }    
}
