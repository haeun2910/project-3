package com.example.project_3.service;

import com.example.project_3.dto.UserDto;
import com.example.project_3.entity.Shop;
import com.example.project_3.entity.User;
import com.example.project_3.repo.ShopRepository;
import com.example.project_3.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ShopService(ShopRepository shopRepository, UserRepository userRepository, UserService userService) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    public List<Shop> getNotOpenShops() {
        return shopRepository.findByOpenStatusTrue();
    }
    public List<Shop> getAllOpenedShops(){
        return shopRepository.findByOpenStatusTrue();
    }
    public Shop getShopById(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            return optionalShop.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
    }
    public Shop getShopByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto user = userService.getUserByUsername(username); // Fetch user by username

        // Assuming user has a method to get the shop
        return shopRepository.findByUserId(user.getId()).orElseThrow();

    }

    public List<Shop> getOwnedShops(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Shop> ownedShops = shopRepository.findByUser(user);

        return ownedShops;
    }


    public Shop updateShopForBusinessUser(Long userId, Shop shop) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Shop> shopOptional = shopRepository.findById(shop.getId());
            if (shopOptional.isPresent()) {
                Shop targetShop = shopOptional.get();
                targetShop.setName(shop.getName());
                targetShop.setDescription(shop.getDescription());
                targetShop.setOpenStatus(shop.isOpenStatus());
                targetShop.setUser(user);  // Set the user as the owner
                return shopRepository.save(targetShop);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission");
        }
    }
    // shop 개설
    public Shop openApply(Long userId, Shop shop) {
        if (shop.getName() == null || shop.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop name cannot be empty");
        }
        if (shop.getDescription() == null || shop.getDescription().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop description cannot be empty");
        }
        if (shop.getCategory() == null || shop.getCategory().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop category cannot be empty");
        }

        Optional<Shop> existingShopOptional = shopRepository.findById(shop.getId());
        if (existingShopOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop does not exist");
        }

        Shop existingShop = existingShopOptional.get();
        if (existingShop.isOpenStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop is already open");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            existingShop.setOpenStatus(false);
            existingShop.setApplicationSubmitted(true);
            existingShop.setUser(userOptional.get());
            return shopRepository.save(existingShop);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission");
        }
    }

    // get application list
    public List<Shop> getApplicationShops() {
        checkIfAdmin();
        return shopRepository.findByApplicationSubmittedTrueAndOpenStatusFalse();
    }

    public void approveShop(Long shopId) {
        checkIfAdmin();

        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();

            if (shop.isApplicationSubmitted()) {
                User applicant = shop.getUser(); // Get the user who applied

                if (applicant != null) {
                    shop.setOpenStatus(true); // Set shop to open status
                    shop.setApplicationSubmitted(false); // Remove application status
                    shop.setUser(applicant); // Set the owner to the applicant

                    shopRepository.save(shop);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Applicant not found");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop application not submitted");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop not found");
        }
    }


    public void rejectShop(Long shopId, String reason) {
        checkIfAdmin();
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent() && shop.get().isApplicationSubmitted()) {
            shop.get().setApplicationSubmitted(false); // 신청 상태 해제
            shopRepository.save(shop.get());
            // 쇼핑몰 주인에게 불허 이유 전달 로직 추가
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid shop rejection request");
        }
    }
    public void requestShopClose(Long shopId, String reason) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            if (shop.getUser() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop owner not found");
            }
            if (!shop.getUser().getAuthorities().contains("ROLE_BUSINESS")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to close this shop");
            }
            // Set close request details
            shop.setCloseRequested(true);
            shop.setCloseReason(reason);
            shopRepository.save(shop);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
    }

    public List<Shop> getShopsWithCloseRequests() {
        checkIfAdmin();
        return shopRepository.findByCloseRequestedTrue();
    }

    public void approveShopClose(Long shopId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            if (shop.isCloseRequested()) {
                // 쇼핑몰을 폐쇄 상태로 변경
                shop.setOpenStatus(false);
                shop.setCloseRequested(false);  // 요청 상태 해제
                shopRepository.save(shop);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No close request found for this shop");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
    }

    private void checkIfAdmin() {
        String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if (!currentUserRole.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have admin privileges");
        }
    }
    }

