/*
 * Final_Project_HR_Web_App
 * ro.sit.hrapp.services  
 * CandidateServiceTestWithConfiguration.java
 * 
 *
 * MADE FOR TRAINING PURPOSES.
 *
 */
package ro.sit.hrapp.services;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ro.sit.hrapp.TestApplicationConfiguration;
import ro.sit.hrapp.service.CandidateService;
import ro.sit.hrapp.service.validator.CandidateRegistrationValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestApplicationConfiguration.class })
public class CandidateServiceTestWithConfiguration extends BaseCandidateServiceTest {

	@Autowired
	private CandidateService service;

	@Autowired
	protected CandidateRegistrationValidator candidateValidator;

	@Override
	protected CandidateService getCandidateService() {
		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.sit.hrapp.services.BaseCompanyServiceTest#getValidator()
	 */
	@Override
	protected CandidateRegistrationValidator getValidator() {
		return candidateValidator;
	}

}
