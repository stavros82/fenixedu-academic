/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherAdviseServiceDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DepartmentAdmOfficeManageTeacherAdviseServiceDispatchAction extends
        ManageTeacherAdviseServiceDispatchAction {

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final ExecutionPeriod executionPeriod = rootDomainObject
                .readExecutionPeriodByOID(executionPeriodID);

        Integer teacherNumber = Integer.valueOf(dynaForm.getString("teacherNumber"));
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDateYearMonthDay(),
                    executionPeriod.getEndDateYearMonthDay());
            if (teacher != null) {
                break;
            }
        }
        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        getAdviseServices(request, dynaForm, executionPeriod, teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        return editAdviseService(form, request, mapping, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        deleteAdviseService(request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");

    }
}
