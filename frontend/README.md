# E-Commerce Frontend

A modern, responsive web interface for the E-Commerce Spring Boot backend.

## 🎨 Features

- **Product Catalog** - Browse and search products
- **Shopping Cart** - Add/remove items, view cart total
- **Order Management** - Create orders, track order history
- **Dual Payment Support** - Mock payment and Razorpay integration
- **Real-time Updates** - Live cart count, order status polling
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Beautiful UI** - Modern gradient design with smooth animations

## 🚀 Quick Start

### Prerequisites

Make sure the backend is running:
1. **Main E-Commerce API** on `http://localhost:8080`
2. **Mock Payment Service** on `http://localhost:8081`

### Running the Frontend

#### Option 1: Simple HTTP Server (Python)
```bash
cd frontend
python -m http.server 8000
```
Then open: http://localhost:8000

#### Option 2: Node.js HTTP Server
```bash
cd frontend
npx http-server -p 8000
```
Then open: http://localhost:8000

#### Option 3: VS Code Live Server
1. Install "Live Server" extension in VS Code
2. Right-click `index.html`
3. Select "Open with Live Server"

#### Option 4: Direct File Access
Simply open `frontend/index.html` in your browser

## 📖 User Guide

### 1. Getting Started

When you first open the app:
1. The default User ID is `user123` (you can change it)
2. Click **"Load Sample Products"** to create 6 sample products in the backend
3. Products will appear in a grid layout

### 2. Shopping Flow

#### Step 1: Browse Products
- View all available products with prices and stock
- Each product shows:
  - Icon representation
  - Name and description
  - Price in INR
  - Available stock
  - Quantity selector

#### Step 2: Add to Cart
- Select quantity (1 to available stock)
- Click "Add to Cart" button
- See cart count badge update in navigation

#### Step 3: View Cart
- Click "Cart" in navigation
- Review all items in cart
- See total items and total amount
- Clear cart if needed

#### Step 4: Create Order
- Click "Create Order" in cart section
- Order will be created from cart items
- Cart will be automatically cleared
- Payment modal will appear

#### Step 5: Process Payment

**Option A: Mock Payment (Easy Testing)**
- Click "Mock Payment (3s delay)"
- Wait 3 seconds for automatic processing
- Order status will change to "PAID"

**Option B: Razorpay Payment**
- Click "Razorpay Payment"
- Payment will be initiated with Razorpay
- In production, user would be redirected to payment page

#### Step 6: View Orders
- Click "Orders" in navigation
- See all your order history
- Orders show:
  - Order ID
  - Status (CREATED, PAID, FAILED)
  - Items ordered
  - Total amount
  - Date & time
- For unpaid orders, click "Pay Now" to retry payment

## 🎯 Features Breakdown

### Product Catalog
- Grid layout with responsive design
- Product icons (auto-detected from name)
- Real-time stock display
- Low stock warning (< 10 items)
- Quantity validation

### Shopping Cart
- Add multiple items
- Update quantities
- View item totals
- Clear entire cart
- Empty cart state

### Order Management
- Create orders from cart
- View order history
- Order status tracking
- Retry payment for pending orders
- Detailed order breakdown

### Payment Processing
- Dual payment modes
- Mock payment for testing (3-second delay)
- Razorpay integration
- Real-time status updates
- Automatic order status polling
- Payment success/failure handling

## 🔧 Configuration

### API Endpoint
Edit `app.js` line 2 to change backend URL:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

### User ID
Change the default user ID in the UI or modify `index.html` line 31:
```html
<input type="text" id="userId" value="user123">
```

### Sample Products
Modify sample products in `app.js` starting at line 17:
```javascript
const sampleProducts = [
    {
        name: 'Gaming Laptop',
        description: 'High-performance gaming laptop',
        price: 125000.0,
        stock: 15
    },
    // Add more products...
];
```

## 🎨 UI Components

### Navigation Bar
- Sticky navigation
- Active section highlighting
- Cart count badge
- Responsive design

### Product Cards
- Icon visualization
- Price formatting
- Stock indicators
- Add to cart action
- Quantity selector

### Cart View
- Item list with details
- Quantity display
- Item and total pricing
- Clear cart option
- Create order button

### Order Cards
- Status badges (color-coded)
- Order ID and timestamp
- Item breakdown
- Total amount
- Retry payment for pending orders

### Payment Modal
- Order summary
- Payment method selection
- Status updates
- Auto-close on success

### Toast Notifications
- Success messages (green)
- Error messages (red)
- Auto-dismiss (3 seconds)
- Slide-in animation

## 📱 Responsive Design

The frontend is fully responsive and works on:
- **Desktop** (1200px+) - Full grid layout
- **Tablet** (768px - 1199px) - Adjusted grid
- **Mobile** (< 768px) - Single column layout

## 🎭 Product Icons

Products automatically get icons based on name:
- Laptop → 💻
- Mouse → 🖱️
- Keyboard → ⌨️
- Monitor → 🖥️
- Headphones → 🎧
- Webcam → 📷
- Phone → 📱
- Default → 📦

## 🐛 Troubleshooting

### Issue: CORS Error
**Symptom:** "Access to fetch blocked by CORS policy"

**Solution:** 
- Make sure backend is running with CORS configuration
- Backend should have `CorsConfig.java` properly configured

### Issue: Products Not Loading
**Symptom:** Empty product grid or error message

**Solution:**
1. Verify backend is running on port 8080
2. Check browser console for errors
3. Try loading sample products
4. Ensure MongoDB is connected

### Issue: Cart Count Not Updating
**Symptom:** Badge shows 0 or wrong count

**Solution:**
- Refresh the page
- Click "Refresh" button in cart section
- Check if correct User ID is set

### Issue: Payment Not Processing
**Symptom:** Payment stuck on "Processing..."

**Solution:**
1. Verify Mock Payment Service is running on port 8081
2. Check backend logs for errors
3. Try refreshing and creating a new order

### Issue: Orders Not Showing
**Symptom:** "No orders yet" message

**Solution:**
- Ensure you're using the same User ID as when creating orders
- Click "Refresh" button
- Check if orders were created successfully

## 📊 Testing Checklist

- [ ] Load sample products successfully
- [ ] Add products to cart
- [ ] View cart with correct totals
- [ ] Create order from cart
- [ ] Process mock payment
- [ ] Verify order status changes to PAID
- [ ] View order in order history
- [ ] Clear cart functionality
- [ ] Multiple user IDs work separately
- [ ] Responsive design on mobile

## 🎓 Educational Value

This frontend demonstrates:
- **REST API integration** - Fetch, POST, DELETE requests
- **Async JavaScript** - Promises, async/await
- **DOM manipulation** - Dynamic content rendering
- **State management** - Global state tracking
- **Error handling** - Try-catch, user feedback
- **Responsive design** - Mobile-first approach
- **UX best practices** - Loading states, empty states
- **Real-time updates** - Polling, auto-refresh

## 🚀 Deployment Tips

For production deployment:

1. **Update API URL**
   - Change `API_BASE_URL` to your production backend
   
2. **Build/Minify**
   - Minify CSS and JavaScript for performance
   
3. **Host Static Files**
   - Deploy to: Netlify, Vercel, GitHub Pages, or any static host
   
4. **HTTPS Required**
   - Use HTTPS for production (especially for payment)
   
5. **Environment Variables**
   - Use different configs for dev/staging/prod

## 📝 Future Enhancements

Possible improvements:
- User authentication/login
- Product search functionality
- Cart persistence (localStorage)
- Order filtering/sorting
- Payment history details
- Product reviews/ratings
- Wishlist feature
- Product images (from URLs)
- Pagination for products/orders
- Advanced filters

## 💡 Tips

1. **Testing Mock Payment:**
   - Mock payment processes in exactly 3 seconds
   - Watch the order status change in real-time

2. **Testing Razorpay:**
   - Requires valid Razorpay credentials in backend
   - Test mode doesn't charge real money

3. **Multiple Users:**
   - Change User ID to test separate carts/orders
   - Each user ID has isolated data

4. **Stock Management:**
   - Creating an order reduces product stock
   - Failed orders don't affect stock
   - Check product stock before large orders

## 📞 Support

If you encounter issues:
1. Check browser console for JavaScript errors
2. Verify backend is running and accessible
3. Check Network tab in browser DevTools
4. Review backend logs for API errors

---

**Enjoy your E-Commerce Frontend! 🛍️**
