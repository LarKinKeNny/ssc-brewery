//package guru.sfg.brewery.bootstrap;
//
//import guru.sfg.brewery.domain.security.Authority;
//import guru.sfg.brewery.domain.security.Role;
//import guru.sfg.brewery.domain.security.User;
//import guru.sfg.brewery.repositories.security.AuthorityRepository;
//import guru.sfg.brewery.repositories.security.RoleRepository;
//import guru.sfg.brewery.repositories.security.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@RequiredArgsConstructor
//@Component
//public class UserDataLoader implements CommandLineRunner {
//
//    private final AuthorityRepository authorityRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final RoleRepository roleRepository;
//
//    @Transactional
//    @Override
//    public void run(String... args) throws Exception {
//        if(authorityRepository.count() == 0) {
//            loadSecurityData();
//        }
//
//        userRepository.findAll().forEach(System.out::println);
//        authorityRepository.findAll().forEach(System.out::println);
//    }
//
//    private void loadSecurityData() {
//        var adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
//        var customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
//        var userRole = roleRepository.save(Role.builder().name("USER").build());
//
//        //beer auths
//        var createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
//        var updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
//        var readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
//        var deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());
//
//        //customer auths
//        var createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
//        var updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
//        var readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
//        var deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());
//
//        //brewery Auth
//        var createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
//        var updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
//        var readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
//        var deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());
//
//        //beer order
//        var createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
//        var updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
//        var readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());
//        var deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());
//
//        var createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
//        var updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
//        var readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());
//        var deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());
//
//
//        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, readBeer, deleteBeer, createCustomer,
//                updateCustomer, readCustomer, deleteCustomer, createBrewery, updateBrewery, readBrewery, deleteBrewery,
//                createOrder, updateOrder, readOrder, deleteOrder)));
//
//        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery, createOrderCustomer,
//                readOrderCustomer, updateOrderCustomer, deleteOrderCustomer)));
//
//        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));
//
//        roleRepository.saveAll(List.of(adminRole, customerRole, userRole));
//
//        var spring = User.builder()
//                .username("spring")
//                .password(passwordEncoder.encode("guru"))
//                .role(adminRole)
//                .build();
//
//        var user = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("password"))
//                .role(userRole)
//                .build();
//
//        var scott = User.builder()
//                .username("scott")
//                .password(passwordEncoder.encode("tiger"))
//                .role(customerRole)
//                .build();
//
//        userRepository.saveAll(List.of(spring, user, scott));
//    }
//}
