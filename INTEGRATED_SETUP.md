# 🎯 Integrated Frontend Setup Guide

The frontend is now **fully integrated** with Spring Boot! Everything runs on one server.

## 🚀 Quick Start (Just 2 Steps!)

### Step 1: Start Backend (2 Terminals)

**Terminal 1 - Main E-Commerce API (with integrated frontend):**
```bash
cd ecommerce
mvn spring-boot:run
```
✅ Wait for: "Started EcommerceApplication"
🌐 Running on: **http://localhost:8080**

**Terminal 2 - Mock Payment Service:**
```bash
cd mock-payment-service
mvn spring-boot:run
```
✅ Wait for: "Started MockPaymentServiceApplication"
🌐 Running on: http://localhost:8081

### Step 2: Open Browser

Navigate to: **http://localhost:8080**

That's it! The frontend is served directly by Spring Boot! 🎉

## ✨ What Changed

### Before (3 Servers):
- ❌ Backend on port 8080
- ❌ Mock Payment on port 8081
- ❌ Frontend on port 8000 (separate HTTP server)

### Now (2 Servers):
- ✅ Backend + Frontend on port 8080 (integrated!)
- ✅ Mock Payment on port 8081

## 📁 File Structure

Frontend files are now in Spring Boot's static resources:

```
ecommerce/
└── src/main/resources/static/
    ├── index.html       # Main UI
    ├── styles.css       # Styling
    └── app.js           # JavaScript (uses relative URLs)
```

## 🎯 Benefits of Integration

1. **Simpler Setup** - One less server to run
2. **No CORS Issues** - Frontend and API on same origin
3. **Production Ready** - Deploy as single JAR file
4. **Faster Development** - No need for separate frontend server
5. **Easier Deployment** - One application, one port

## 🧪 Testing the Complete Flow

### Open Browser: http://localhost:8080

1. **Load Products** - Click "Load Sample Products"
2. **Shop** - Add items to cart
3. **Checkout** - Create order from cart
4. **Pay** - Process payment (Mock or Razorpay)
5. **Verify** - Check order status

## 📝 How It Works

### Spring Boot Static Resource Serving

Spring Boot automatically serves static files from:
- `src/main/resources/static/`
- `src/main/resources/public/`

Our files:
- `http://localhost:8080/` → `index.html`
- `http://localhost:8080/styles.css` → `styles.css`
- `http://localhost:8080/app.js` → `app.js`

### API Endpoints (same server)

JavaScript uses **relative URLs**:
```javascript
const API_BASE_URL = '/api';  // Same server!
```

So:
- `fetch('/api/products')` → `http://localhost:8080/api/products`
- `fetch('/api/cart/add')` → `http://localhost:8080/api/cart/add`

No CORS needed! ✅

## 🎨 Accessing the UI

### Homepage
```
http://localhost:8080/
```
or
```
http://localhost:8080/index.html
```

### Direct Access to Static Files
```
http://localhost:8080/styles.css
http://localhost:8080/app.js
```

### API Endpoints (still work!)
```
http://localhost:8080/api/products
http://localhost:8080/api/cart/user123
http://localhost:8080/api/orders
```

## 🔧 Development Workflow

### Making Frontend Changes

1. Edit files in `src/main/resources/static/`
2. Restart Spring Boot application
3. Refresh browser

**Hot Reload (Optional):**
```bash
# Add Spring Boot DevTools to pom.xml for auto-reload
```

### Testing API Separately

You can still test APIs with Postman:
```
POST http://localhost:8080/api/products
GET  http://localhost:8080/api/cart/user123
```

## 📦 Deployment

### Build JAR (includes frontend!)

```bash
mvn clean package
```

### Run JAR

```bash
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

The JAR file contains:
- ✅ Backend code
- ✅ Frontend files (HTML/CSS/JS)
- ✅ All dependencies

Deploy this single JAR to any server! 🚀

## 🎯 Production Deployment

### Deploy to Cloud

**Heroku:**
```bash
git push heroku main
```

**AWS Elastic Beanstalk:**
```bash
eb deploy
```

**Docker:**
```dockerfile
FROM openjdk:17
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables

```bash
# Set in production
SPRING_DATA_MONGODB_URI=mongodb+srv://...
RAZORPAY_KEY_ID=rzp_live_...
RAZORPAY_KEY_SECRET=...
```

## 🐛 Troubleshooting

### Frontend Not Loading

**Issue:** Blank page at http://localhost:8080

**Solutions:**
1. Check files exist in `src/main/resources/static/`
2. Restart Spring Boot application
3. Clear browser cache
4. Check browser console for errors

### API Calls Failing

**Issue:** "404 Not Found" for API calls

**Solutions:**
1. Verify backend is running
2. Check API endpoints are correct
3. Look at browser Network tab
4. Check backend logs

### Static Files Not Updating

**Issue:** Changes not reflected

**Solutions:**
1. Restart Spring Boot (required after static file changes)
2. Hard refresh browser (Ctrl+F5)
3. Clear browser cache
4. Check you're editing files in `src/main/resources/static/`

## ✅ Verification Checklist

- [ ] Backend running on http://localhost:8080
- [ ] Mock Payment Service running on http://localhost:8081
- [ ] Frontend accessible at http://localhost:8080
- [ ] Can load sample products
- [ ] Can add items to cart
- [ ] Can create orders
- [ ] Can process payments
- [ ] Can view order history

## 🎓 For Grading/Demo

### Start Application

```bash
# Terminal 1
cd ecommerce
mvn spring-boot:run

# Terminal 2
cd mock-payment-service
mvn spring-boot:run
```

### Demo URL

Open: **http://localhost:8080**

Everything is in one place! ✨

## 💡 Pro Tips

1. **Single Port** - Only need to remember port 8080
2. **Same Origin** - No CORS configuration needed
3. **Simple Deploy** - One JAR file contains everything
4. **Mobile Friendly** - Responsive design works great
5. **Production Ready** - Deploy anywhere Spring Boot runs

## 🚀 Next Steps

### Add More Features

Edit files in `src/main/resources/static/`:
- `index.html` - Add new sections
- `styles.css` - Customize design
- `app.js` - Add functionality

### Enhance Backend

Add new API endpoints in Spring Boot controllers

### Deploy to Production

Build JAR and deploy to cloud platform

---

**Enjoy your integrated e-commerce application! 🎉**

*Everything runs on http://localhost:8080 - simple and powerful!*
