package data;

import com.iremodelapi.domain.*;
import com.iremodelapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ContractorRepository contractorRepository;
    private final HomeownerRepository homeownerRepository;
    private final JobRepository jobRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      ContractorRepository contractorRepository,
                      HomeownerRepository homeownerRepository,
                      JobRepository jobRepository,
                      ReviewRepository reviewRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.contractorRepository = contractorRepository;
        this.homeownerRepository = homeownerRepository;
        this.jobRepository = jobRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception
    {
        // Only load data if database is empty
        if (userRepository.count() == 0) {
            System.out.println("Loading demo data for presentation...");
            loadDemoData();
            System.out.println("Demo data loaded successfully!");
        } else {
            System.out.println("Demo data already exists, skipping data load.");
        }
    }

    private void loadDemoData()
    {
        // Create Homeowners
        List<Homeowner> homeowners = createHomeowners();

        // Create Contractors
        List<Contractor> contractors = createContractors();

        // Create Jobs
        List<Job> jobs = createJobs(homeowners);

        // Create Reviews (establishes contractor ratings)
        createReviews(homeowners, contractors, jobs);

        // Update contractor ratings based on reviews
        updateContractorRatings(contractors);
    }

    private List<Homeowner> createHomeowners()
    {
        System.out.println("Creating homeowners...");

        Homeowner homeowner1 = new Homeowner();
        homeowner1.setFirstName("Sarah");
        homeowner1.setLastName("Johnson");
        homeowner1.setEmail("sarah.johnson@email.com");
        homeowner1.setPassword(passwordEncoder.encode("password123"));
        homeowner1.setPhoneNumber("5551234567");
        homeowner1.setZipCode("12345");
        homeowner1.setRole(User.UserRole.HOMEOWNER);
        homeowner1.setMailingAddress("123 Oak Street, Springfield, IL 62701");
        homeowner1.setPreferredContactMethod("Email");
        homeowner1.setCreatedAt(LocalDateTime.now());
        homeowner1.setUpdatedAt(LocalDateTime.now());

        Homeowner homeowner2 = new Homeowner();
        homeowner2.setFirstName("Michael");
        homeowner2.setLastName("Chen");
        homeowner2.setEmail("michael.chen@email.com");
        homeowner2.setPassword(passwordEncoder.encode("password123"));
        homeowner2.setPhoneNumber("5552345678");
        homeowner2.setZipCode("12346");
        homeowner2.setRole(User.UserRole.HOMEOWNER);
        homeowner2.setMailingAddress("456 Maple Ave, Springfield, IL 62702");
        homeowner2.setPreferredContactMethod("Phone");
        homeowner2.setCreatedAt(LocalDateTime.now());
        homeowner2.setUpdatedAt(LocalDateTime.now());

        Homeowner homeowner3 = new Homeowner();
        homeowner3.setFirstName("Emily");
        homeowner3.setLastName("Rodriguez");
        homeowner3.setEmail("emily.rodriguez@email.com");
        homeowner3.setPassword(passwordEncoder.encode("password123"));
        homeowner3.setPhoneNumber("5553456789");
        homeowner3.setZipCode("12350");
        homeowner3.setRole(User.UserRole.HOMEOWNER);
        homeowner3.setMailingAddress("789 Pine Street, Decatur, IL 62521");
        homeowner3.setPreferredContactMethod("Email");
        homeowner3.setCreatedAt(LocalDateTime.now());
        homeowner3.setUpdatedAt(LocalDateTime.now());

        return homeownerRepository.saveAll(Arrays.asList(homeowner1, homeowner2, homeowner3));
    }

    private List<Contractor> createContractors()
    {
        System.out.println("Creating contractors...");

        // Kitchen Specialist
        Contractor contractor1 = new Contractor();
        contractor1.setFirstName("David");
        contractor1.setLastName("Wilson");
        contractor1.setEmail("david@kitchenpros.com");
        contractor1.setPassword(passwordEncoder.encode("password123"));
        contractor1.setPhoneNumber("5554567890");
        contractor1.setZipCode("12345");
        contractor1.setRole(User.UserRole.CONTRACTOR);
        contractor1.setCompanyName("Kitchen Pros LLC");
        contractor1.setDescription("Specializing in kitchen remodels and cabinet installation for over 15 years.");
        contractor1.setAddress("100 Commercial Drive, Springfield, IL 62701");
        contractor1.setLicenseNumber("IL-KIT-001");
        contractor1.addSpecialty(Contractor.Specialty.REMODELING);
        contractor1.setInsured(true);
        contractor1.setCompletedJobsCount(45);
        contractor1.setRating(4.8);
        contractor1.setCreatedAt(LocalDateTime.now());
        contractor1.setUpdatedAt(LocalDateTime.now());

        // Bathroom Specialist
        Contractor contractor2 = new Contractor();
        contractor2.setFirstName("Lisa");
        contractor2.setLastName("Thompson");
        contractor2.setEmail("lisa@bathdesigns.com");
        contractor2.setPassword(passwordEncoder.encode("password123"));
        contractor2.setPhoneNumber("5555678901");
        contractor2.setZipCode("12346");
        contractor2.setRole(User.UserRole.CONTRACTOR);
        contractor2.setCompanyName("Bath Design Solutions");
        contractor2.setDescription("Creating beautiful, functional bathrooms with attention to detail.");
        contractor2.setAddress("200 Design Boulevard, Springfield, IL 62702");
        contractor2.setLicenseNumber("IL-BAT-002");
        contractor2.addSpecialty(Contractor.Specialty.PLUMBING);
        contractor2.setInsured(true);
        contractor2.setCompletedJobsCount(32);
        contractor2.setRating(4.6);
        contractor2.setCreatedAt(LocalDateTime.now());
        contractor2.setUpdatedAt(LocalDateTime.now());

        // Roofing Specialist
        Contractor contractor3 = new Contractor();
        contractor3.setFirstName("Robert");
        contractor3.setLastName("Martinez");
        contractor3.setEmail("robert@peakroofing.com");
        contractor3.setPassword(passwordEncoder.encode("password123"));
        contractor3.setPhoneNumber("5556789012");
        contractor3.setZipCode("12345");
        contractor3.setRole(User.UserRole.CONTRACTOR);
        contractor3.setCompanyName("Peak Roofing Services");
        contractor3.setDescription("Professional roofing installation and repair with 20+ years experience.");
        contractor3.setAddress("300 Industrial Park, Springfield, IL 62701");
        contractor3.setLicenseNumber("IL-ROO-003");
        contractor3.addSpecialty(Contractor.Specialty.ROOFING);
        contractor3.setInsured(true);
        contractor3.setCompletedJobsCount(78);
        contractor3.setRating(4.9);
        contractor3.setCreatedAt(LocalDateTime.now());
        contractor3.setUpdatedAt(LocalDateTime.now());

        // Flooring Specialist
        Contractor contractor4 = new Contractor();
        contractor4.setFirstName("Amanda");
        contractor4.setLastName("Davis");
        contractor4.setEmail("amanda@floormaster.com");
        contractor4.setPassword(passwordEncoder.encode("password123"));
        contractor4.setPhoneNumber("5557890123");
        contractor4.setZipCode("12346");
        contractor4.setRole(User.UserRole.CONTRACTOR);
        contractor4.setCompanyName("FloorMaster Installation");
        contractor4.setDescription("Expert hardwood, tile, and luxury vinyl flooring installation.");
        contractor4.setAddress("400 Craftsman Lane, Springfield, IL 62702");
        contractor4.setLicenseNumber("IL-FLO-004");
        contractor4.addSpecialty(Contractor.Specialty.FLOORING);
        contractor4.setInsured(true);
        contractor4.setCompletedJobsCount(56);
        contractor4.setRating(4.7);
        contractor4.setCreatedAt(LocalDateTime.now());
        contractor4.setUpdatedAt(LocalDateTime.now());

        // Electrical Specialist
        Contractor contractor5 = new Contractor();
        contractor5.setFirstName("James");
        contractor5.setLastName("Anderson");
        contractor5.setEmail("james@sparkelectric.com");
        contractor5.setPassword(passwordEncoder.encode("password123"));
        contractor5.setPhoneNumber("5558901234");
        contractor5.setZipCode("12350");
        contractor5.setRole(User.UserRole.CONTRACTOR);
        contractor5.setCompanyName("Spark Electric Solutions");
        contractor5.setDescription("Licensed electrician providing residential electrical services and upgrades.");
        contractor5.setAddress("500 Electric Avenue, Decatur, IL 62521");
        contractor5.setLicenseNumber("IL-ELE-005");
        contractor5.addSpecialty(Contractor.Specialty.ELECTRICAL);
        contractor5.setInsured(true);
        contractor5.setCompletedJobsCount(89);
        contractor5.setRating(4.5);
        contractor5.setCreatedAt(LocalDateTime.now());
        contractor5.setUpdatedAt(LocalDateTime.now());

        // General Contractor
        Contractor contractor6 = new Contractor();
        contractor6.setFirstName("Carlos");
        contractor6.setLastName("Gomez");
        contractor6.setEmail("carlos@buildbetter.com");
        contractor6.setPassword(passwordEncoder.encode("password123"));
        contractor6.setPhoneNumber("5559012345");
        contractor6.setZipCode("12345");
        contractor6.setRole(User.UserRole.CONTRACTOR);
        contractor6.setCompanyName("Build Better Contracting");
        contractor6.setDescription("Full-service general contracting for home additions and major renovations.");
        contractor6.setAddress("600 Builder Street, Springfield, IL 62701");
        contractor6.setLicenseNumber("IL-GEN-006");
        contractor6.addSpecialty(Contractor.Specialty.GENERAL);
        contractor6.setInsured(true);
        contractor6.setCompletedJobsCount(67);
        contractor6.setRating(4.4);
        contractor6.setCreatedAt(LocalDateTime.now());
        contractor6.setUpdatedAt(LocalDateTime.now());

        return contractorRepository.saveAll(Arrays.asList(
                contractor1, contractor2, contractor3, contractor4, contractor5, contractor6));
    }

    private List<Job> createJobs(List<Homeowner> homeowners)
    {
        System.out.println("Creating jobs...");

        // Kitchen Remodel Job (should match Kitchen Pros LLC perfectly)
        Job job1 = new Job();
        job1.setTitle("Modern Kitchen Remodel");
        job1.setDescription("Complete kitchen renovation including new cabinets, countertops, appliances, and flooring. Looking for a contractor with experience in modern kitchen designs.");
        job1.setCategory(Job.JobCategory.REMODELING);
        job1.setBudget(new BigDecimal("35000.00"));
        job1.setZipCode("12345");
        job1.setStatus(Job.JobStatus.OPEN);
        job1.setHomeowner(homeowners.get(0));
        job1.setCreatedAt(LocalDateTime.now());
        job1.setUpdatedAt(LocalDateTime.now());

        // Bathroom Renovation Job
        Job job2 = new Job();
        job2.setTitle("Master Bathroom Renovation");
        job2.setDescription("Full master bathroom remodel with new tile, fixtures, vanity, and shower. Seeking experienced bathroom contractor.");
        job2.setCategory(Job.JobCategory.PLUMBING);
        job2.setBudget(new BigDecimal("18000.00"));
        job2.setZipCode("12346");
        job2.setStatus(Job.JobStatus.OPEN);
        job2.setHomeowner(homeowners.get(1));
        job2.setCreatedAt(LocalDateTime.now());
        job2.setUpdatedAt(LocalDateTime.now());

        // Roofing Job
        Job job3 = new Job();
        job3.setTitle("Roof Replacement");
        job3.setDescription("Need complete roof replacement due to storm damage. Asphalt shingles preferred. Insurance approved.");
        job3.setCategory(Job.JobCategory.ROOFING);
        job3.setBudget(new BigDecimal("15000.00"));
        job3.setZipCode("12345");
        job3.setStatus(Job.JobStatus.OPEN);
        job3.setHomeowner(homeowners.get(0));
        job3.setCreatedAt(LocalDateTime.now());
        job3.setUpdatedAt(LocalDateTime.now());

        // Flooring Job
        Job job4 = new Job();
        job4.setTitle("Hardwood Floor Installation");
        job4.setDescription("Install hardwood flooring in living room, dining room, and hallway. Approximately 800 square feet.");
        job4.setCategory(Job.JobCategory.FLOORING);
        job4.setBudget(new BigDecimal("12000.00"));
        job4.setZipCode("12346");
        job4.setStatus(Job.JobStatus.OPEN);
        job4.setHomeowner(homeowners.get(1));
        job4.setCreatedAt(LocalDateTime.now());
        job4.setUpdatedAt(LocalDateTime.now());

        // Electrical Job
        Job job5 = new Job();
        job5.setTitle("Electrical Panel Upgrade");
        job5.setDescription("Upgrade electrical panel from 100amp to 200amp service. Need licensed electrician for permit and inspection.");
        job5.setCategory(Job.JobCategory.ELECTRICAL);
        job5.setBudget(new BigDecimal("3500.00"));
        job5.setZipCode("12350");
        job5.setStatus(Job.JobStatus.OPEN);
        job5.setHomeowner(homeowners.get(2));
        job5.setCreatedAt(LocalDateTime.now());
        job5.setUpdatedAt(LocalDateTime.now());

        // General Construction Job
        Job job6 = new Job();
        job6.setTitle("Home Addition Project");
        job6.setDescription("Add 400 sq ft family room addition with foundation, framing, electrical, and finishing work.");
        job6.setCategory(Job.JobCategory.GENERAL);
        job6.setBudget(new BigDecimal("75000.00"));
        job6.setZipCode("12345");
        job6.setStatus(Job.JobStatus.OPEN);
        job6.setHomeowner(homeowners.get(0));
        job6.setCreatedAt(LocalDateTime.now());
        job6.setUpdatedAt(LocalDateTime.now());

        return jobRepository.saveAll(Arrays.asList(job1, job2, job3, job4, job5, job6));
    }

    private void createReviews(List<Homeowner> homeowners, List<Contractor> contractors, List<Job> jobs)
    {
        System.out.println("Creating reviews...");

        // Create some sample reviews to establish contractor ratings
        Review review1 = new Review();
        review1.setHomeowner(homeowners.get(0));
        review1.setContractor(contractors.get(0)); // Kitchen Pros
        review1.setJob(jobs.get(0));
        review1.setRating(5);
        review1.setComment("Outstanding kitchen remodel! Professional, timely, and exceeded our expectations. Highly recommend Kitchen Pros LLC.");
        review1.setCreatedAt(LocalDateTime.now().minusDays(30));

        Review review2 = new Review();
        review2.setHomeowner(homeowners.get(1));
        review2.setContractor(contractors.get(1)); // Bath Design
        review2.setJob(jobs.get(1));
        review2.setRating(4);
        review2.setComment("Good quality work on our bathroom renovation. Communication could have been better, but final result was satisfactory.");
        review2.setCreatedAt(LocalDateTime.now().minusDays(20));

        Review review3 = new Review();
        review3.setHomeowner(homeowners.get(0));
        review3.setContractor(contractors.get(2)); // Peak Roofing
        review3.setJob(jobs.get(2));
        review3.setRating(5);
        review3.setComment("Exceptional roofing work! Peak Roofing was professional, cleaned up thoroughly, and completed ahead of schedule.");
        review3.setCreatedAt(LocalDateTime.now().minusDays(15));

        reviewRepository.saveAll(Arrays.asList(review1, review2, review3));
    }

    private void updateContractorRatings(List<Contractor> contractors)
    {
        System.out.println("Updating contractor ratings...");

        // In a real application, you might calculate these based on actual reviews
        // For demo purposes, we'll set realistic ratings that were already set in createContractors()

        contractorRepository.saveAll(contractors);
    }
}