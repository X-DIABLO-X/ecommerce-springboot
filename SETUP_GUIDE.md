# Quick Setup Guide - E-Commerce Backend API

## 🎯 What You Need to Setup

### 1. MongoDB (Choose One Option)

#### Option A: Local MongoDB (Recommended for Development)
```bash
# Download from: https://www.mongodb.com/try/download/community

# Windows - Install and start MongoDB service
# Linux/Mac - Install via package manager
sudo apt install mongodb  # Ubuntu/Debian
brew install mongodb-community  # Mac

# Start MongoDB
mongod --dbpath /path/to/data
```

#### Option B: MongoDB Atlas (Cloud - Easiest)
1. Go to https://www.mongodb.com/cloud/atlas
2. Create FREE account
3. Create FREE cluster (M0 - 512MB)
4. Click "Connect" → "Connect your application"
5. Copy connection string (looks like: `mongodb+srv://username:password@cluster...`)
6. Use this in `application.properties`

### 2. Razorpay Account (For Payment Integration)

1. **Sign Up:**
   - Go to https://razorpay.com
   - Create account (use any email)

2. **Get Test Keys:**
   - Login to Dashboard
   - Go to Settings → API Keys
   - Generate Test Mode Keys
   - Copy:
     - Key ID (starts with `rzp_test_`)
     - Key Secret

3. **Configure in Application:**
   ```properties
   razorpay.key.id=rzp_test_YOUR_KEY_ID
   razorpay.key.secret=YOUR_KEY_SECRET
   ```

### 3. Java & Maven

```bash
# Check if installed
java -version  # Should be Java 17 or higher
mvn -version   # Should be Maven 3.6+

# If not installed:
# Download JDK: https://www.oracle.com/java/technologies/downloads/
# Download Maven: https://maven.apache.org/download.cgi
```

## 🚀 Running the Application

### Step 1: Configure Application

Edit `src/main/resources/application.properties`:

```properties
# MongoDB - Use ONE of these:
spring.data.mongodb.uri=mongodb://localhost:27017/ecommerce
# OR
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/ecommerce

# Razorpay (from dashboard)
razorpay.key.id=rzp_test_YOUR_KEY_ID
razorpay.key.secret=YOUR_KEY_SECRET

# Leave these as is
mock.payment.service.url=http://localhost:8081
server.port=8080
```

### Step 2: Start Main Application

```bash
# Terminal 1 - Main E-Commerce App
cd ecommerce
mvn clean install
mvn spring-boot:run
```

Wait for: `Started EcommerceApplication in X seconds`

### Step 3: Start Mock Payment Service

```bash
# Terminal 2 - Mock Payment Service
cd mock-payment-service
mvn clean install
mvn spring-boot:run
```

Wait for: `Started MockPaymentServiceApplication in X seconds`

### Step 4: Test with Postman

1. **Import Collection:**
   - Open Postman
   - Import `Ecommerce-API.postman_collection.json`

2. **Run Complete Flow:**
   - Navigate to folder: "6. Complete Flow Test"
   - Run requests in order:
     1. Step 1: Create Product
     2. Step 2: Add to Cart
     3. Step 3: View Cart
     4. Step 4: Create Order
     5. Step 5: Create Payment (Mock)
     6. Wait 3 seconds
     7. Step 6: Check Order Status → Should be "PAID"

## 🧪 Testing Checklist

| Step | Endpoint | Expected Result |
|------|----------|----------------|
| 1 | POST /api/products | 201 Created, returns product ID |
| 2 | POST /api/cart/add | 201 Created, item added |
| 3 | GET /api/cart/{userId} | 200 OK, shows cart items |
| 4 | POST /api/orders | 201 Created, status: CREATED |
| 5 | POST /api/payments/create | 201 Created, payment initiated |
| 6 | GET /api/orders/{orderId} | 200 OK, status: PAID ✅ |

## 🎓 APIs to Test for Grading

### 1. Product APIs (15 points)
```bash
POST http://localhost:8080/api/products
GET  http://localhost:8080/api/products
```

### 2. Cart APIs (20 points)
```bash
POST   http://localhost:8080/api/cart/add
GET    http://localhost:8080/api/cart/{userId}
DELETE http://localhost:8080/api/cart/{userId}/clear
```

### 3. Order APIs (25 points)
```bash
POST http://localhost:8080/api/orders
GET  http://localhost:8080/api/orders/{orderId}
```

### 4. Payment APIs (30 points)
```bash
POST http://localhost:8080/api/payments/create
```

### 5. Webhook (10 points)
```bash
POST http://localhost:8080/api/webhooks/payment
```

## 🐛 Common Issues

### Issue 1: MongoDB Connection Failed
```
Error: MongoTimeoutException
```
**Fix:**
- Make sure MongoDB is running: `mongosh` or check Atlas connection
- Verify connection string in `application.properties`

### Issue 2: Port Already in Use
```
Error: Port 8080 already in use
```
**Fix:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Issue 3: Razorpay Initialization Failed
```
Error: Failed to initialize Razorpay client
```
**Fix:**
- Check if key ID starts with `rzp_test_`
- Verify key secret is correct (no extra spaces)
- Make sure you're using TEST mode keys

### Issue 4: Mock Payment Service Not Working
```
Error: Connection refused to localhost:8081
```
**Fix:**
- Ensure Mock Payment Service is running in separate terminal
- Check `mvn spring-boot:run` in `mock-payment-service` directory

## 📊 Environment Variables Setup (Optional but Recommended)

Instead of hardcoding in `application.properties`:

**Windows:**
```cmd
set RAZORPAY_KEY_ID=rzp_test_YOUR_KEY
set RAZORPAY_KEY_SECRET=YOUR_SECRET
mvn spring-boot:run
```

**Linux/Mac:**
```bash
export RAZORPAY_KEY_ID=rzp_test_YOUR_KEY
export RAZORPAY_KEY_SECRET=YOUR_SECRET
mvn spring-boot:run
```

## 🎁 Bonus Points Testing

### Product Search (+5 points)
```bash
GET http://localhost:8080/api/products/search?q=laptop
```

### User Order History (+5 points)
```bash
GET http://localhost:8080/api/orders/user/{userId}
```

### Razorpay Integration (+10 points)
```bash
POST http://localhost:8080/api/payments/create
Body: { "orderId": "...", "amount": 100000, "paymentMode": "RAZORPAY" }
```

## 📞 Need Help?

1. Check application logs in terminal for detailed errors
2. Verify all prerequisites are installed
3. Make sure both services (main + mock) are running
4. Test each API individually before complete flow

---

**Good Luck with Your Assignment! 🎓**
