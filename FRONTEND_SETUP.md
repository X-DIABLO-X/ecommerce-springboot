# Frontend Setup & Testing Guide

Quick guide to get the frontend running and test the complete e-commerce flow.

## 🚀 Quick Setup (3 Steps)

### Step 1: Start Backend Services

**Terminal 1 - Main API:**
```bash
cd ecommerce
mvn spring-boot:run
```
Wait for: "Started EcommerceApplication"
Running on: http://localhost:8080

**Terminal 2 - Mock Payment Service:**
```bash
cd mock-payment-service
mvn spring-boot:run
```
Wait for: "Started MockPaymentServiceApplication"
Running on: http://localhost:8081

### Step 2: Start Frontend

**Terminal 3 - Frontend:**
```bash
cd frontend
python -m http.server 8000
```
Or use Node.js:
```bash
npx http-server -p 8000
```

**Alternative:** Just double-click `frontend/index.html`

### Step 3: Open in Browser

Navigate to: **http://localhost:8000**

## 🧪 Complete Test Flow (5 Minutes)

### Test 1: Load Sample Products

1. Click **"Load Sample Products"** button
2. Wait 2-3 seconds
3. ✅ You should see 6 products appear:
   - Gaming Laptop (₹1,25,000)
   - Wireless Mouse (₹2,500)
   - Mechanical Keyboard (₹8,500)
   - 4K Monitor (₹35,000)
   - Gaming Headphones (₹5,500)
   - HD Webcam (₹4,500)

### Test 2: Add Items to Cart

1. For "Gaming Laptop":
   - Change quantity to **2**
   - Click "Add to Cart"
   - ✅ Cart badge shows "2"

2. For "Wireless Mouse":
   - Keep quantity as **1**
   - Click "Add to Cart"
   - ✅ Cart badge shows "3"

### Test 3: View Shopping Cart

1. Click **"Cart"** in navigation
2. ✅ You should see:
   - Gaming Laptop x 2 = ₹2,50,000
   - Wireless Mouse x 1 = ₹2,500
   - **Total Items: 3**
   - **Total Amount: ₹2,52,500**

### Test 4: Create Order

1. In cart section, click **"Create Order"**
2. ✅ Order created successfully
3. ✅ Payment modal appears showing:
   - Order ID
   - Amount: ₹2,52,500
   - Payment options

### Test 5: Process Mock Payment

1. In payment modal, click **"Mock Payment (3s delay)"**
2. ✅ Status shows: "Processing payment..."
3. Wait 3 seconds ⏰
4. ✅ Status changes to: "Payment successful!"
5. Modal auto-closes

### Test 6: Verify Order

1. Click **"Orders"** in navigation
2. ✅ You should see your order:
   - Status: **PAID** (green badge)
   - Items: Gaming Laptop x 2, Wireless Mouse x 1
   - Total: ₹2,52,500
   - Date & time

### Test 7: Check Product Stock

1. Click **"Products"** in navigation
2. ✅ Gaming Laptop stock reduced from 15 to **13**
3. ✅ Wireless Mouse stock reduced from 50 to **49**

## ✅ Complete Test Checklist

- [ ] Backend API running (port 8080)
- [ ] Mock Payment Service running (port 8081)
- [ ] Frontend accessible (port 8000 or file://)
- [ ] 6 sample products loaded
- [ ] Products show correct icons and prices
- [ ] Cart badge updates when adding items
- [ ] Cart shows correct items and totals
- [ ] Order created successfully from cart
- [ ] Cart cleared after order creation
- [ ] Payment modal appears
- [ ] Mock payment processes in ~3 seconds
- [ ] Order status changes to PAID
- [ ] Order appears in order history
- [ ] Product stock decreases correctly

## 🎯 Advanced Testing

### Test Different User IDs

1. Change User ID to `user456`
2. Click "Refresh"
3. Add different products to cart
4. Create separate order
5. ✅ Each user has isolated cart and orders

### Test Razorpay Payment

1. Ensure Razorpay credentials configured in backend
2. Create an order
3. Click "Razorpay Payment" in modal
4. ✅ Payment initiated with Razorpay order ID

### Test Error Scenarios

**Empty Cart:**
1. Clear cart
2. Try to create order
3. ✅ Error: "Cart is empty"

**Insufficient Stock:**
1. Try to add 100 laptops (stock only 13)
2. ✅ Error: "Insufficient stock"

**Pending Order Payment:**
1. Create an order but close payment modal
2. Go to Orders section
3. ✅ Order shows status "CREATED" with "Pay Now" button
4. Click "Pay Now" to retry payment

## 🐛 Common Issues & Fixes

### Issue: "Error loading products"
```
❌ Backend not running or MongoDB not connected
✅ Start backend and verify MongoDB connection
```

### Issue: CORS error in console
```
❌ CORS configuration missing
✅ Verify CorsConfig.java exists in backend
✅ Restart backend after adding CORS config
```

### Issue: Payment stuck on "Processing"
```
❌ Mock Payment Service not running
✅ Start mock service on port 8081
✅ Check if port 8081 is available
```

### Issue: Cart count shows 0
```
❌ Using different User ID
✅ Ensure same User ID used throughout session
✅ Click "Refresh" button
```

## 📊 Expected Results

### After Complete Flow:

**Products:**
- 6 products created ✅
- Stock reduced for ordered items ✅

**Cart:**
- Empty (cleared after order) ✅
- Badge shows 0 ✅

**Orders:**
- 1 order with status PAID ✅
- Correct items and total ✅

**Database (MongoDB):**
- 6 documents in `products` collection
- 0 documents in `cart_items` collection (cleared)
- 1 document in `orders` collection
- 1 document in `payments` collection

## 🎓 Grading Demo Flow

Perfect flow to demonstrate for assignment:

1. **Show Empty State**
   - Open fresh frontend
   - Show no products initially

2. **Load Sample Data**
   - Click "Load Sample Products"
   - Show 6 products appear

3. **Shopping Flow**
   - Add 2-3 different products to cart
   - Show cart count updating
   - Navigate to cart, show totals

4. **Order Creation**
   - Create order from cart
   - Show cart cleared
   - Show payment modal

5. **Payment Processing**
   - Choose Mock Payment
   - Show 3-second processing
   - Show success message

6. **Order Verification**
   - Navigate to Orders
   - Show order with PAID status
   - Show correct items and total

7. **Stock Update**
   - Navigate back to Products
   - Show reduced stock quantities

**Total Demo Time:** ~3-4 minutes

## 🚀 Production Checklist

Before deploying to production:

- [ ] Update `API_BASE_URL` to production backend
- [ ] Enable HTTPS
- [ ] Configure production Razorpay keys
- [ ] Add authentication/authorization
- [ ] Implement cart persistence
- [ ] Add error boundary/fallback UI
- [ ] Optimize images and assets
- [ ] Add loading skeletons
- [ ] Implement retry logic for failed requests
- [ ] Add analytics tracking

## 💡 Pro Tips

1. **Keep DevTools Open**
   - Monitor Network tab for API calls
   - Check Console for errors
   - Use Application tab to inspect state

2. **Test Edge Cases**
   - Try quantity = 0
   - Try negative quantities
   - Add same product multiple times
   - Create multiple orders

3. **Performance Testing**
   - Load 50+ products
   - Add 20+ items to cart
   - Create 10+ orders

4. **Browser Compatibility**
   - Test on Chrome, Firefox, Safari
   - Test on mobile devices
   - Test different screen sizes

---

**Ready to Demo! 🎉**
