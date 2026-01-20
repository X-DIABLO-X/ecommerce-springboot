# 🚀 START HERE - E-Commerce Application with Razorpay

## Quick Start (1 Terminal Only!)

### ⚡ Fastest Way to Get Started

#### Start Backend + Frontend (All-in-One!)
```bash
cd ecommerce
mvn spring-boot:run
```
Wait for: `Started EcommerceApplication`

### 🌐 Open Browser

Go to: **http://localhost:8080**

That's it! Everything runs on ONE server! 🎉

## 📋 Prerequisites (First Time Setup)

### 1. MongoDB - Must be running
- **Local:** `mongod --dbpath /path/to/data`
- **Cloud:** Use MongoDB Atlas (free tier available)

### 2. Razorpay API Keys (Required!)

**Get your FREE test keys:**
1. Sign up at https://razorpay.com (no credit card needed)
2. Login to Dashboard
3. Go to **Settings → API Keys**
4. Make sure you're in **Test Mode**
5. Click **"Generate Test Keys"**
6. Copy both:
   - Key ID: `rzp_test_XXXXXXXXXX`
   - Key Secret: `XXXXXXXXXXXXXXXXXXX`

### 3. Configure Backend

Edit `src/main/resources/application.properties`:
```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/ecommerce

# Razorpay (REPLACE with your actual keys!)
razorpay.key.id=rzp_test_YOUR_KEY_ID_HERE
razorpay.key.secret=YOUR_KEY_SECRET_HERE
```

⚠️ **Important:** Replace placeholder values with actual Razorpay keys!

## 🎯 What You Get

### One Application, Everything Included:
- ✅ Backend REST API (Spring Boot)
- ✅ Frontend Web UI (served by Spring Boot!)
- ✅ Razorpay Payment Integration
- ✅ MongoDB Database
- ✅ Complete E-Commerce Flow

### Accessible At:
- **Frontend:** http://localhost:8080
- **API:** http://localhost:8080/api/*
- **Webhook:** http://localhost:8080/api/webhooks/razorpay

## 🧪 Quick Test (30 Seconds)

1. Open http://localhost:8080
2. Click "Load Sample Products"
3. Add items to cart
4. Click Cart → Create Order
5. Click "Proceed to Payment"
6. ✅ Razorpay order created with Order ID

## 📚 Documentation

- **[RAZORPAY_SETUP.md](RAZORPAY_SETUP.md)** - **START HERE** for Razorpay configuration
- **[README.md](README.md)** - Complete documentation
- **[INTEGRATED_SETUP.md](INTEGRATED_SETUP.md)** - How frontend integration works
- **[API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)** - API reference

## 🎨 Features

### Frontend (http://localhost:8080)
- Product catalog with grid layout
- Shopping cart with live updates
- Order creation and tracking
- Payment processing (Mock/Razorpay)
- Order history
- Beautiful responsive design

### Backend APIs
- Product management
- Cart operations
- Order processing
- Payment integration
- Webhook handling

## 🐛 Troubleshooting

### Issue: Can't access http://localhost:8080
**Solution:** 
- Check if backend is running
- Look for "Started EcommerceApplication" in terminal
- Try http://127.0.0.1:8080

### Issue: "Error loading products"
**Solution:**
- Verify MongoDB is running
- Check MongoDB connection string
- Click "Load Sample Products" to create test data

### Issue: "Authentication failed" error
**Solution:**
- Check Razorpay API keys are configured correctly
- Make sure keys start with `rzp_test_`
- See [RAZORPAY_SETUP.md](RAZORPAY_SETUP.md) for details
- Restart application after updating keys

## ✅ Verify Setup

Run this checklist:
- [ ] MongoDB running and accessible
- [ ] Razorpay test API keys configured
- [ ] Backend started successfully
- [ ] Can access http://localhost:8080
- [ ] Can see the TechStore UI
- [ ] Can click "Load Sample Products"
- [ ] Can see 6 products appear
- [ ] Can create payment (no authentication error)

## 🎓 For Assignment/Grading

### Demo Flow
1. Show integrated UI at http://localhost:8080
2. Load sample products
3. Complete purchase flow
4. Show PAID order

### What to Submit
- ✅ Complete source code
- ✅ Postman collection
- ✅ Screenshots of working UI
- ✅ README with setup instructions

### Grading Points
- Backend APIs: 100 points
- Razorpay Integration: +10 bonus
- Product Search: +5 bonus
- Order History: +5 bonus
- **Integrated Frontend: Extra credit!** 🎨

## 💡 Pro Tips

1. **Single Port** - Everything on http://localhost:8080
2. **Hot Reload** - Install Spring DevTools for auto-reload
3. **Sample Data** - Use "Load Sample Products" button
4. **Testing** - Use browser for UI, Postman for API
5. **Deploy** - Build single JAR with `mvn package`

## 🚀 Next Steps

### Explore
- Try different payment modes
- Test with multiple users (change User ID)
- Check order history
- Search products

### Customize
- Edit `src/main/resources/static/` for UI changes
- Add new APIs in controllers
- Modify sample products in `app.js`

### Deploy
```bash
# Build deployable JAR
mvn clean package

# Run anywhere
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

---

## 🎉 You're Ready!

Just run 1 command and open your browser!

```bash
# Terminal - Start everything!
cd ecommerce && mvn spring-boot:run

# Browser - Access the app!
http://localhost:8080
```

**Don't forget to configure Razorpay keys first!** See [RAZORPAY_SETUP.md](RAZORPAY_SETUP.md)

**Happy coding! 🚀**
