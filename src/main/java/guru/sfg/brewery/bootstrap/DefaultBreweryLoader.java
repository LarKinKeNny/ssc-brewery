/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jt on 2019-01-26.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    public static final String DUNEDIN_DISTRIBUTING = "Dunedin Distributing";
    public static final String KEY_WEST_DISTRIBUTING = "Key West Distributing";

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final String STPETE_USER = "stpete";
    public static final String KEYWEST_USER = "keywest";
    public static final String DUNEDIN_USER = "dunedin";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        loadSecurityData();
        loadBreweryData();
        loadTastingRoomData();
        loadCustomers();
    }

    private void loadCustomers() {
        var customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();

        //create Customers
        var stPeteCustomer = customerRepository.save(Customer.builder().customerName(ST_PETE_DISTRIBUTING)
                .apiKey(UUID.randomUUID()).build());
        var dunedinCustomer = customerRepository.save(Customer.builder().customerName(DUNEDIN_DISTRIBUTING)
                .apiKey(UUID.randomUUID()).build());
        var kwCustomer = customerRepository.save(Customer.builder().customerName(KEY_WEST_DISTRIBUTING)
                .apiKey(UUID.randomUUID()).build());

        //create users
        var stPeteUser = userRepository.save(User.builder()
                .username("stpete")
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(customerRole)
                .build());

        var dunedinUser = userRepository.save(User.builder()
                .username("dunedin")
                .password(passwordEncoder.encode("password"))
                .customer(dunedinCustomer)
                .role(customerRole)
                .build());

        var kwUser = userRepository.save(User.builder()
                .username("keywest")
                .password(passwordEncoder.encode("password"))
                .customer(kwCustomer)
                .role(customerRole)
                .build());

        //create orders
        createOrder(stPeteCustomer);
        createOrder(dunedinCustomer);
        createOrder(kwCustomer);

    }

    private BeerOrder createOrder(Customer customer) {
        return beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(new HashSet<>(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2).build())))
                .build());
    }

    private void loadTastingRoomData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer ->
                beerOrderRepository.save(BeerOrder.builder()
                        .customer(tastingRoom)
                        .orderStatus(OrderStatusEnum.NEW)
                        .beerOrderLines(Set.of(BeerOrderLine.builder()
                                .beer(beer)
                                .orderQuantity(2)
                                .build()))
                        .build()));
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .id(UUID.fromString("eb0cc5bc-3850-4d72-aa67-72df0b05b420"))
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .id(UUID.fromString("7e4a991b-e94a-49e7-896f-1184667cb546"))
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .id(UUID.fromString("0c6c549c-1b6e-488e-a54d-582136c8a388"))
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }

    private void loadSecurityData() {
        var adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        var customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        var userRole = roleRepository.save(Role.builder().name("USER").build());

        //beer auths
        var createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        var updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        var readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        var deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        //customer auths
        var createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        var updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        var readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
        var deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        //brewery Auth
        var createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        var updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        var readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        var deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        //beer order
        var createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
        var updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
        var readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());
        var deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());

        var pickupOrder = authorityRepository.save(Authority.builder().permission("order.pickup").build());

        var createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
        var updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
        var readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());
        var deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());
        var pickupOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.pickup").build());


        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, readBeer, deleteBeer, createCustomer,
                updateCustomer, readCustomer, deleteCustomer, createBrewery, updateBrewery, readBrewery, deleteBrewery,
                createOrder, updateOrder, readOrder, deleteOrder, pickupOrder)));

        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery, createOrderCustomer,
                readOrderCustomer, updateOrderCustomer, deleteOrderCustomer, pickupOrderCustomer)));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(List.of(adminRole, customerRole, userRole));

        var spring = User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole)
                .build();

        var user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build();

        var scott = User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole)
                .build();

        userRepository.saveAll(List.of(spring, user, scott));
    }
}
