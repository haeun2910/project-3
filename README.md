
# Shopping Mall System

## Overview

This project implements a **shopping mall management system** with user authentication and role-based permissions. The system allows users to sign up, apply to become business owners, and manage their shops and products. Admin users can review and approve shop applications. The client-side is built using **HTML, CSS, and JavaScript**, while the backend is powered by **Spring Boot** and follows a **JWT-based authentication** system.

## Features

### User Authentication and Role Management

- **JWT Authentication:** Users authenticate via a token-based system. After login, users receive a JWT token, which they must use for further authenticated requests.
- **User Roles:**
  - **Inactive User:** The initial state after registration.
  - **Regular User:** Converted from inactive users after completing their profile.
  - **Business User:** Regular users can apply to become business owners.
  - **Admin:** Handles shop approvals and other administrative tasks.

### User Management

- **Registration:** Users can sign up with an email, password, and other profile details (nickname, age group, phone number, etc.).
- **Profile Management:** Users can upload profile pictures and update their personal information.
- **Role Transition:** 
  - Inactive users become regular users by completing required profile fields.
  - Regular users can apply to become business owners, pending admin approval.

### Shopping Mall Operations

- **Shop Creation:**
  - Business users can apply to open shops.
  - Shops contain a name, description, and category (e.g., electronics, clothing, etc.).
  - Admins can review shop applications and approve or deny them with reasons.
  - Approved shops move to the **Open** status.
- **Shop Management:**
  - Shop owners can edit shop details and manage their products.
  - Products have essential fields like name, image, description, price, and stock.

### Product Search and Purchase

- **Product Search:** Users can search for products by name and price range.
- **Shop Search:** Users can filter shops by category or name.
- **Purchases:**
  - Users can request to purchase products, and shop owners must confirm payment before finalizing the transaction.
  - Inventory updates automatically upon successful purchases.
  - Purchases can be canceled before confirmation by the shop owner.

## Client-Side Implementation

- **Technologies Used:** HTML, CSS, and JavaScript.
- The client interfaces with the backend to handle user authentication, shop management, and product transactions.
  
## Installation and Setup

1. **Backend Setup:**
    - Clone the repository and navigate to the backend directory.
    - Run `mvn clean install` to build the project.
    - Start the application using `mvn spring-boot:run`.
    - Ensure the application is running on `http://localhost:8080`.

2. **Frontend Setup:**
    - Open the `index.html` in a browser.
    - Ensure the backend is running to allow the frontend to make API requests.

## How to Use

1. **User Authentication:**
   - Register via the sign-up page.
   - Log in using the registered credentials to receive a JWT token.
   - Use this token for authenticated actions such as viewing profiles, applying for a shop, and managing products.

2. **Shop Management:**
   - Regular users can apply to become business owners from their profile page.
   - Business owners can create, edit, and manage their shops and products.

3. **Admin Actions:**
   - Admins can view shop applications and approve or deny them.

## Challenges Faced

- **JWT Authentication:** Handling token-based authentication was initially complex, but it became smoother once the token flow was fully implemented.
- **Role Management:** Managing transitions between different user roles (inactive, regular, business) required additional logic, especially around automatic state changes.

## Future Enhancements

- Implementing an order history for users.
- Enhancing the search functionality with additional filters like product categories.

Here’s an updated version of the **Challenges Faced** section, reflecting the difficulties encountered with `userId`, `shopId`, and `productId` in the JS code:

---

## Challenges Faced
  
- **Role Management:** Managing transitions between different user roles (inactive, regular, business) required additional logic. Automatically updating users' roles upon completing profile details or applying to become a business owner involved building backend checks and notifications to ensure a smooth transition.

- **Handling IDs in JavaScript:** One of the more challenging aspects was managing `userId`, `shopId`, and `productId` in the frontend JS code when making API requests. Since these IDs often needed to be passed through multiple layers of the application (such as retrieving `userId` to apply for a shop or handling `shopId` when adding products), it became difficult to keep track of which ID was needed for a specific operation. 
    - **Example 1:** When submitting a product addition form, ensuring the correct `shopId` was included in the request body required careful handling of state and DOM elements.
    - **Example 2:** Retrieving `userId` from JWT tokens was challenging because the token had to be decoded on the client side before making authenticated requests. 
    - **Example 3:** For product-related actions, such as adding, editing, or deleting a product, ensuring the right `productId` was passed correctly between routes and forms caused some confusion, especially when the same page managed multiple operations.

- **Form Data Handling:** Dynamically managing form data when adding or editing shops and products was tricky, especially when dealing with file uploads (for profile images or product images). Ensuring the backend could correctly handle multipart requests took several iterations of debugging.
