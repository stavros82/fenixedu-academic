package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile;
import net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationPeriodBean;
import net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationUploadBean;
import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/genericApplications", module = "publico")
@Forwards({ @Forward(name = "genericApplications.listApplicationPeriods", path = "genericApplications.listApplicationPeriods"),
        @Forward(name = "genericApplications.viewApplicationPeriod", path = "genericApplications.viewApplicationPeriod"),
        @Forward(name = "genericApplications.confirmEmail", path = "genericApplications.confirmEmail") })
public class GenericCandidaciesDA extends FenixDispatchAction {

    public ActionForward listApplicationPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final SortedSet<GenericApplicationPeriod> periods = GenericApplicationPeriod.getPeriods();
        request.setAttribute("periods", periods);

        final IUserView userView = AccessControl.getUserView();
        if (userView != null && userView.hasRoleType(RoleType.MANAGER)) {
            final GenericApplicationPeriodBean genericApplicationPeriodBean = new GenericApplicationPeriodBean();
            request.setAttribute("genericApplicationPeriodBean", genericApplicationPeriodBean);
        }

        return mapping.findForward("genericApplications.listApplicationPeriods");
    }

    public ActionForward viewApplicationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final GenericApplicationPeriod applicationPeriod = getDomainObject(request, "applicationPeriodId");
        request.setAttribute("applicationPeriod", applicationPeriod);

        return mapping.findForward("genericApplications.viewApplicationPeriod");
    }

    public ActionForward createApplicationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationPeriodBean bean = getRenderedObject("genericApplicationPeriodBean");
        bean.createNewPeriod();
        return listApplicationPeriods(mapping, form, request, response);
    }

    public void justCreateApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationPeriod period = getDomainObject(request, "periodOid");
        final String email = (String) getFromRequest(request, "email");
        final GenericApplication application = period.createApplication(email);
        request.setAttribute("sentEmailForApplication", application);
    }

    public ActionForward createApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        justCreateApplication(mapping, form, request, response);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward createApplicationFromPeriodPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        justCreateApplication(mapping, form, request, response);
        return viewApplicationPeriod(mapping, form, request, response);
    }

    public ActionForward confirmEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)) {
            request.setAttribute("application", application);
            request.setAttribute("uploadBean", new GenericApplicationUploadBean());
            return mapping.findForward("genericApplications.confirmEmail");
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward saveApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        request.setAttribute("applicationSaved", Boolean.TRUE);
        return confirmEmail(mapping, form, request, response);
    }

    public ActionForward uploadDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationUploadBean uploadBean = getRenderedObject("genericApplicationDocumentUploadFormFile");
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        uploadBean.uploadTo(application);
        RenderUtils.invalidateViewState();
        return confirmEmail(mapping, form, request, response);
    }

    public ActionForward downloadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationFile file = getDomainObject(request, "fileExternalId");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)
                && file != null && file.getGenericApplication() == application) {
            response.setContentType(file.getMimeType());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
            response.setContentLength(file.getSize());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContents());
            dos.close();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return null;
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationFile file = getDomainObject(request, "fileExternalId");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)
                && file != null && file.getGenericApplication() == application) {
            file.deleteFromApplication();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return confirmEmail(mapping, form, request, response);
    }

}
