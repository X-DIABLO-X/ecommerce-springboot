# API Testing Guide - Quick Reference

## 🎯 Environment Setup Required

### 1. MongoDB
- **Local**: Install MongoDB and run on `localhost:27017`
- **Cloud**: Sign up for MongoDB Atlas (free tier) and get connection string

### 2. Razorpay Account
- Sign up at: https://razorpay.com
- Navigate to: Settings → API Keys
- Generate Test Mode keys
- You'll need:
  - **Key ID**: `rzp_test_XXXXXXXXXX`
  - **Key Secret**: `XXXXXXXXXXXXXXXXXXX`

### 3. Update Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/ecommerce
razorpay.key.id=rzp_test_YOUR_KEY_ID
razorpay.key.secret=YOUR_KEY_SECRET
```

## 🚀 Starting the Applications

### Terminal 1: Main E-Commerce API
```bash
cd ecommerce
mvn spring-boot:run
```
Runs on: **http://localhost:8080**

### Terminal 2: Mock Payment Service
```bash
cd mock-payment-service
mvn spring-boot:run
```
Runs on: **http://localhost:8081**

## 📡 Complete API List for Testing

### 1. Product APIs ✅

#### Create Product
```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}
```

#### Get All Products
```http
GET http://localhost:8080/api/products
```

#### Get Product by ID
```http
GET http://localhost:8080/api/products/{productId}
```

#### Search Products (Bonus +5 pts)
```http
GET http://localhost:8080/api/products/search?q=laptop
```

---

### 2. Cart APIs ✅

#### Add Item to Cart
```http
POST http://localhost:8080/api/cart/add
Content-Type: application/json

{
  "userId": "user123",
  "productId": "65abc...",
  "quantity": 2
}
```

#### Get User Cart
```http
GET http://localhost:8080/api/cart/user123
```

#### Clear Cart
```http
DELETE http://localhost:8080/api/cart/user123/clear
```

---

### 3. Order APIs ✅

#### Create Order from Cart
```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "userId": "user123"
}
```

**Important:** This will:
- Fetch cart items
- Validate stock
- Create order with status "CREATED"
- Clear the cart
- Return order with orderId

#### Get Order Details
```http
GET http://localhost:8080/api/orders/{orderId}
```

#### Get User Order History (Bonus +5 pts)
```http
GET http://localhost:8080/api/orders/user/user123
```

---

### 4. Payment APIs ✅

#### Option A: Create Mock Payment (Easy Testing)
```http
POST http://localhost:8080/api/payments/create
Content-Type: application/json

{
  "orderId": "order_65xyz...",
  "amount": 100000.0,
  "paymentMode": "MOCK"
}
```

**What happens:**
1. Payment created with status "PENDING"
2. Mock service called
3. After 3 seconds, webhook automatically triggers
4. Order status changes to "PAID"

#### Option B: Create Razorpay Payment (Bonus +10 pts)
```http
POST http://localhost:8080/api/payments/create
Content-Type: application/json

{
  "orderId": "order_65xyz...",
  "amount": 100000.0,
  "paymentMode": "RAZORPAY"
}
```

**Response includes:**
```json
{
  "id": "pay_123",
  "razorpayOrderId": "order_razorpay_abc",
  "status": "PENDING"
}
```

---

### 5. Webhook APIs ✅

#### Mock Payment Webhook (Auto-triggered or Manual)
```http
POST http://localhost:8080/api/webhooks/payment
Content-Type: application/json

{
  "orderId": "order_65xyz...",
  "status": "SUCCESS",
  "paymentId": "mock_pay_12345"
}
```

#### Razorpay Webhook (For Testing)
```http
POST http://localhost:8080/api/webhooks/payment
Content-Type: application/json

{
  "event": "payment.captured",
  "payload": {
    "payment": {
      "entity": {
        "id": "pay_razorpay_123",
        "order_id": "order_razorpay_abc",
        "status": "captured"
      }
    }
  }
}
```

---

## 🧪 Complete End-to-End Test Flow

### Test Scenario: Buy 2 Laptops

1. **Create Product**
   ```bash
   POST /api/products
   → Save productId from response
   ```

2. **Add to Cart**
   ```bash
   POST /api/cart/add
   → Use productId, quantity: 2
   ```

3. **View Cart**
   ```bash
   GET /api/cart/user123
   → Verify items in cart
   ```

4. **Create Order**
   ```bash
   POST /api/orders
   → Save orderId from response
   → Verify status: "CREATED"
   → Cart should be empty now
   ```

5. **Create Payment (Mock)**
   ```bash
   POST /api/payments/create
   → Use orderId, paymentMode: "MOCK"
   ```

6. **Wait 3 Seconds** ⏰
   ```
   Mock service processes payment...
   Webhook sent automatically...
   ```

7. **Verify Order Status**
   ```bash
   GET /api/orders/{orderId}
   → Verify status: "PAID" ✅
   → Verify payment details included
   ```

---

## 📊 Grading Checklist

| Component | Points | API to Test |
|-----------|--------|-------------|
| Create Product | 10 | POST /api/products |
| List Products | 5 | GET /api/products |
| Add to Cart | 10 | POST /api/cart/add |
| View Cart | 5 | GET /api/cart/{userId} |
| Clear Cart | 5 | DELETE /api/cart/{userId}/clear |
| Create Order | 15 | POST /api/orders |
| Get Order | 10 | GET /api/orders/{orderId} |
| Create Payment | 20 | POST /api/payments/create |
| Webhook Handling | 10 | POST /api/webhooks/payment |
| Order Status Update | 10 | Verify in GET /api/orders/{orderId} |
| **TOTAL** | **100** | |
| **BONUS: Product Search** | +5 | GET /api/products/search?q=laptop |
| **BONUS: Order History** | +5 | GET /api/orders/user/{userId} |
| **BONUS: Razorpay** | +10 | paymentMode: "RAZORPAY" |

---

## 🎯 Quick Testing Commands (Postman)

### Variables to Set
```
baseUrl = http://localhost:8080
userId = user123
productId = (copy from create product response)
orderId = (copy from create order response)
```

### Testing Order
1. Products → Create Laptop Product
2. Cart → Add Laptop to Cart
3. Cart → Get Cart Items
4. Orders → Create Order from Cart
5. Payment → Create Mock Payment
6. Wait 3 seconds
7. Orders → Get Order Details (verify status = PAID)

---

## 🐛 Common Issues & Solutions

### Issue: "Cart is empty"
**Solution:** Make sure you added items to cart before creating order

### Issue: "Insufficient stock"
**Solution:** Check product stock is greater than quantity in cart

### Issue: "Order not found"
**Solution:** Use correct orderId from create order response

### Issue: "Order is not in CREATED status"
**Solution:** Don't try to pay for already paid/failed orders. Create new order.

### Issue: Mock webhook not working
**Solution:** 
- Ensure Mock Payment Service is running on port 8081
- Check logs in both terminals
- Try manual webhook call

---

## 📝 Sample Test Data

### Product 1: Laptop
```json
{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}
```

### Product 2: Mouse
```json
{
  "name": "Mouse",
  "description": "Wireless Mouse",
  "price": 1000.0,
  "stock": 50
}
```

### Product 3: Keyboard
```json
{
  "name": "Keyboard",
  "description": "Mechanical Keyboard",
  "price": 3000.0,
  "stock": 30
}
```

---

## ✅ Final Verification

Before submission, verify:
- [ ] MongoDB is running and connected
- [ ] Both services (main + mock) are running
- [ ] All Product APIs work
- [ ] All Cart APIs work
- [ ] All Order APIs work
- [ ] Mock Payment works (3-second delay)
- [ ] Webhook updates order status to PAID
- [ ] Razorpay integration works (if implemented)
- [ ] Postman collection imported and tested

---

**Ready to Test! 🚀**
