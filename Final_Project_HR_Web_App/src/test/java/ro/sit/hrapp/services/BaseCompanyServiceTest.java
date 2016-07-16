/*
 * Final_Project_HR_Web_App
 * ro.sit.hrapp.services  
 * a.java
 * 
 *
 * MADE FOR TRAINING PURPOSES.
 * 
 * MADE SOME REFACTORING @SorinDRAGAN
 * 
 */

package ro.sit.hrapp.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import ro.sit.hrapp.domain.Company;
import ro.sit.hrapp.domain.JobDescription;
import ro.sit.hrapp.domain.JobDescription.CurrentJobTitle;
import ro.sit.hrapp.domain.JobDescription.Location;
import ro.sit.hrapp.domain.JobDescription.PersonalSkills;
import ro.sit.hrapp.domain.JobDescription.ProfessionalSkills;
import ro.sit.hrapp.domain.JobDescription.YearsOfExperience;
import ro.sit.hrapp.service.CandidateService;
import ro.sit.hrapp.service.CompanyService;
import ro.sit.hrapp.service.validator.CompanyRegistrationValidator;

/**
 * 
 * @author stefan
 * 
 */
public abstract class BaseCompanyServiceTest {

	protected abstract CompanyService getCompanyService();

	protected abstract CompanyRegistrationValidator getValidator();

	CompanyService companyService = new CompanyService();
	JobDescription jobList = new JobDescription();

	@After
	public void tearDown() {
		Collection<Company> companies = getCompanyService().listAll();

		for (Company company : companies) {
			getCompanyService().deleteCompany(company.getId());
		}
	}

	@Test
	public void test_empty_get_all() {
		Collection<Company> company = getCompanyService().listAll();
		Assert.assertTrue(company.isEmpty());
	}

	@Test
	public void addCompany() {
		// given
		jobList = createJobDescriptionObject(JobDescription.CurrentJobTitle.BA,
				JobDescription.YearsOfExperience.ZERO_TO_ONE, JobDescription.Location.CLUJ_NAPOCA,
				listProfessionalSkills(), listPersonalSkills());
		Company company = createObjectFromCompany("nokia", "nokya", "phones", "nokia.emp@yahoo.com", "1234567890",
				"phones", jobList, "Cluj-Napoca");
		Errors errors = new BeanPropertyBindingResult(company, "company");
		// when
		getValidator().validate(company, errors);
		getCompanyService().saveCompany(company);
		Collection<Company> comp = getCompanyService().listAll();
		List<Company> companyList = new ArrayList<>(comp);
		// then
		System.out.println(errors.toString());
		assertFalse(errors.hasErrors()); // validate fields from company
		assertEquals("nokia", companyList.get(0).getCompanyName());
		assertTrue(companyList.size() == 1);

	}

	@Test
	public void deleteCompany() {
		// given
		jobList = createJobDescriptionObject(JobDescription.CurrentJobTitle.BA,
				JobDescription.YearsOfExperience.ZERO_TO_ONE, JobDescription.Location.CLUJ_NAPOCA,
				listProfessionalSkills(), listPersonalSkills());
		Company companyToDelete = createObjectFromCompany("endava", "nokya", "phones", "nokia.emp@yahoo.com",
				"1234567890", "phones", jobList, "Cluj-Napoca");
		Errors errors = new BeanPropertyBindingResult(companyToDelete, "company");
		// when
		getValidator().validate(companyToDelete, errors); // validate fields
															// from company
		getCompanyService().saveCompany(companyToDelete);
		Collection<Company> companies = getCompanyService().listAll();
		Long id = companyToDelete.getId();
		getCompanyService().deleteCompany(id);
		// then
		System.out.println(errors.toString());
		assertFalse(errors.hasErrors());
		assertTrue(companies.isEmpty());

	}

	@Test
	public void findCompanyById() {
		// given
		jobList = createJobDescriptionObject(JobDescription.CurrentJobTitle.BA,
				JobDescription.YearsOfExperience.ZERO_TO_ONE, JobDescription.Location.CLUJ_NAPOCA,
				listProfessionalSkills(), listPersonalSkills());
		Company company = createObjectFromCompany("nokia", "nokya", "phones", "nokia.emp@yahoo.com", "1234567890",
				"phones", jobList, "Cluj-Napoca");
		Errors errors = new BeanPropertyBindingResult(company, "company");
		getCompanyService().saveCompany(company);
		// when
		Long companyId = company.getId();
		assertEquals(company, getCompanyService().getCompanyDAO().findByIdCompany(companyId));

	}

	

	@Test
	public void test_add_no_company_name() {
		// given
		jobList = createJobDescriptionObject(JobDescription.CurrentJobTitle.BA,
				JobDescription.YearsOfExperience.ZERO_TO_ONE, JobDescription.Location.CLUJ_NAPOCA,
				listProfessionalSkills(), listPersonalSkills());
		Company company = createObjectFromCompany("", "nokya", "phones", "nokia.emp@yahoo.com", "1234567890", "phones",
				jobList, "Cluj-Napoca");
		Errors errors = new BeanPropertyBindingResult(company, "company");
		// when
		getValidator().validate(company, errors); // validate fields from
													// company
		getCompanyService().saveCompany(company);
		// then
		assertEquals("Please enter your company name.", errors.getFieldError().getDefaultMessage());

		getCompanyService().getCompanyDAO().deleteCompany(company);

	}

	@Test
	public void test_add_no_username() {

		jobList = createJobDescriptionObject(JobDescription.CurrentJobTitle.BA,
				JobDescription.YearsOfExperience.ZERO_TO_ONE, JobDescription.Location.CLUJ_NAPOCA,
				listProfessionalSkills(), listPersonalSkills());
		Company company = createObjectFromCompany("nokia", "", "phones", "nokia.emp@yahoo.com", "1234567890", "phones",
				jobList, "Cluj-Napoca");
		Errors errors = new BeanPropertyBindingResult(company, "company");
		// when
		getValidator().validate(company, errors); // validate fields from
													// company
		getCompanyService().saveCompany(company);
		// then
		assertEquals("Please enter your username.", errors.getFieldError().getDefaultMessage());

		getCompanyService().getCompanyDAO().deleteCompany(company);
	}

	private JobDescription createJobDescriptionObject(CurrentJobTitle ba, YearsOfExperience zeroToOne,
			Location clujNapoca, List<ProfessionalSkills> profesionalSkils, List<PersonalSkills> personalSkils) {

		JobDescription jd = new JobDescription();
		jd.setCurrentJobTitle(ba);
		jd.setYearOfExperience(zeroToOne);
		jd.setLocation(clujNapoca);
		jd.setProfessionalSkills(profesionalSkils);
		jd.setPersonalSkills(personalSkils);
		return jd;
	}

	private Company createObjectFromCompany(String companyName, String userName, String password, String email,
			String phone, String confirmedPassword, JobDescription jobList, String jobLocation) {

		Company company = new Company();
		company.setCompanyName(companyName);
		company.setUserName(userName);
		company.setPassword(password);
		company.setEmail(email);
		company.setPhoneNumber(phone);
		company.setPasswordConfirmed(confirmedPassword);
		company.setJobDescription(jobList);
		company.setJobLocation(jobLocation);
		return company;
	}

	private List<ProfessionalSkills> listProfessionalSkills() {
		List<ProfessionalSkills> proSkils = new LinkedList<ProfessionalSkills>();
		proSkils.add(JobDescription.ProfessionalSkills.JAVA);
		proSkils.add(JobDescription.ProfessionalSkills.SPRING);
		proSkils.add(JobDescription.ProfessionalSkills.JDBC);

		return proSkils;
	}

	private List<PersonalSkills> listPersonalSkills() {
		List<PersonalSkills> persSkils = new LinkedList<PersonalSkills>();
		persSkils.add(JobDescription.PersonalSkills.GOOD_COMMNUNICATOR);
		persSkils.add(JobDescription.PersonalSkills.TEAM_PLAYER);
		persSkils.add(JobDescription.PersonalSkills.GOOD_LISTENER);
		return persSkils;
	}

}
