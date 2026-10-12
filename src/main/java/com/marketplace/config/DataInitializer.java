package com.marketplace.config;

import com.marketplace.model.Role;
import com.marketplace.model.ServiceCategory;
import com.marketplace.model.User;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer seeds default service categories and demo accounts on first launch.
 *
 * Viva Notes:
 * - CommandLineRunner: Spring Boot interface that runs its 'run' method automatically
 *   after the application context is loaded.
 * - This provides seed data without having to execute manual SQL scripts.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedCategories();
        seedDefaultUsers();
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            logger.info("Database empty: Seeding default service categories...");

            categoryRepository.save(new ServiceCategory(
                    "Plumbing",
                    "Pipe leakage, tap replacement, bathroom fittings, and drainage repairs",
                    "Wrench"
            ));

            categoryRepository.save(new ServiceCategory(
                    "Electrical Repairs",
                    "Wiring issues, switchboard repair, appliance installation, and MCB tripping",
                    "Zap"
            ));

            categoryRepository.save(new ServiceCategory(
                    "AC Repair & Service",
                    "AC regular servicing, gas refilling, cooling diagnostics, and installation",
                    "Wind"
            ));

            categoryRepository.save(new ServiceCategory(
                    "Vehicle Repair",
                    "Two-wheeler and four-wheeler roadside breakdown, servicing, and battery check",
                    "Car"
            ));

            categoryRepository.save(new ServiceCategory(
                    "Home Cleaning",
                    "Deep house cleaning, kitchen and bathroom disinfection, sofa sanitization",
                    "Sparkles"
            ));

            categoryRepository.save(new ServiceCategory(
                    "Painting & Waterproofing",
                    "Interior and exterior wall painting, waterproof coating, and plaster touchups",
                    "Paintbrush"
            ));

            categoryRepository.save(new ServiceCategory(
                    "Computer & Laptop Repair",
                    "Hardware diagnostics, OS installation, virus removal, and screen replacement",
                    "Laptop"
            ));

            logger.info("Seeded 7 default service categories successfully!");
        }
    }

    private void seedDefaultUsers() {
        // Seed default Admin if not exists
        if (!userRepository.existsByEmail("admin@marketplace.com")) {
            User admin = new User(
                    "System Admin",
                    "admin@marketplace.com",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN,
                    "9876543210"
            );
            userRepository.save(admin);
            logger.info("Seeded Admin account: admin@marketplace.com / admin123");
        }

        // Seed demo Customer if not exists
        if (!userRepository.existsByEmail("customer@demo.com")) {
            User demoCustomer = new User(
                    "Rahul Sharma",
                    "customer@demo.com",
                    passwordEncoder.encode("demo123"),
                    Role.CUSTOMER,
                    "9876543211"
            );
            userRepository.save(demoCustomer);
            logger.info("Seeded Demo Customer: customer@demo.com / demo123");
        }

        // Seed demo Provider if not exists
        if (!userRepository.existsByEmail("provider@demo.com")) {
            User demoProvider = new User(
                    "Suresh Expert Plumber",
                    "provider@demo.com",
                    passwordEncoder.encode("demo123"),
                    Role.PROVIDER,
                    "9876543212"
            );
            userRepository.save(demoProvider);
            logger.info("Seeded Demo Provider: provider@demo.com / demo123");
        }
    }
}
