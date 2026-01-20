# 🎯 Razorpay Setup Guide

This application uses **Razorpay** for payment processing. Follow this guide to configure Razorpay.

## 📋 Prerequisites

You need a Razorpay account (free signup, no credit card required for test mode).

## 🚀 Quick Setup

### Step 1: Create Razorpay Account

1. Go to https://razorpay.com
2. Click **"Sign Up"**
3. Fill in your details (use any email)
4. Verify your email
5. Login to Dashboard

### Step 2: Get Test API Keys

1. In Razorpay Dashboard, go to **Settings** (gear icon)
2. Click **API Keys** in left sidebar
3. Make sure you're in **Test Mode** (toggle at top)
4. Click **"Generate Test Keys"**
5. You'll see two keys:
   - **Key ID**: `rzp_test_XXXXXXXXXX`
   - **Key Secret**: `XXXXXXXXXXXXXXXXXXX`
6. **Copy both keys** - you'll need them next

### Step 3: Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Razorpay Configuration
razorpay.key.id=rzp_test_YOUR_KEY_ID_HERE
razorpay.key.secret=YOUR_KEY_SECRET_HERE
```

**Replace the placeholder values** with your actual keys from Step 2.

### Step 4: Restart Application

```bash
# Stop the application (Ctrl+C in terminal)
# Start again
mvn spring-boot:run
```

✅ Razorpay is now configured!

## 🧪 Testing Payments

### Test with UI

1. Open http://localhost:8080
2. Load sample products
3. Add items to cart
4. Create order
5. Click **"Proceed to Payment"**
6. Razorpay order will be created
7. You'll see the Razorpay Order ID

### Test Card Details (Razorpay Test Mode)

When testing in Razorpay, use these test card numbers:

**Success:**
- Card: `4111 1111 1111 1111`
- CVV: Any 3 digits
- Expiry: Any future date
- Name: Any name

**Card requiring OTP:**
- Card: `5267 3181 8797 5449`
- OTP: `1234`

**Failed Payment:**
- Card: `4000 0000 0000 0002`

More test cards: https://razorpay.com/docs/payments/payments/test-card-details/

## 🔗 Webhook Configuration (Optional)

For production or advanced testing, configure webhooks:

### Local Testing with ngrok

1. **Install ngrok:**
   ```bash
   # Download from https://ngrok.com/download
   ```

2. **Start ngrok:**
   ```bash
   ngrok http 8080
   ```

3. **Copy the https URL** (e.g., `https://abc123.ngrok.io`)

4. **Configure in Razorpay:**
   - Go to Settings → Webhooks
   - Click "Add New Webhook"
   - URL: `https://abc123.ngrok.io/api/webhooks/razorpay`
   - Events: Select `payment.captured`, `payment.failed`
   - Secret: Leave blank (or set for security)
   - Click "Create Webhook"

### Production Webhook

For production deployment:
- Webhook URL: `https://yourdomain.com/api/webhooks/razorpay`
- Enable webhook signature verification
- Use secret key for validation

## 🔐 Security Best Practices

### 1. Use Environment Variables (Recommended)

Instead of hardcoding in `application.properties`:

**Windows:**
```cmd
set RAZORPAY_KEY_ID=rzp_test_YOUR_KEY_ID
set RAZORPAY_KEY_SECRET=YOUR_KEY_SECRET
mvn spring-boot:run
```

**Linux/Mac:**
```bash
export RAZORPAY_KEY_ID=rzp_test_YOUR_KEY_ID
export RAZORPAY_KEY_SECRET=YOUR_KEY_SECRET
mvn spring-boot:run
```

### 2. Never Commit Secrets

Add to `.gitignore`:
```
application.properties
.env
```

Use `application.properties.example` instead:
```properties
razorpay.key.id=YOUR_KEY_ID_HERE
razorpay.key.secret=YOUR_KEY_SECRET_HERE
```

### 3. Test vs Live Mode

- **Test Mode**: For development (keys start with `rzp_test_`)
- **Live Mode**: For production (keys start with `rzp_live_`)

⚠️ Never use Live mode keys in development!

## 📊 Payment Flow

```
1. User creates order
   ↓
2. Frontend calls /api/payments/create
   ↓
3. Backend creates Razorpay order
   ↓
4. Razorpay returns order ID
   ↓
5. User completes payment on Razorpay
   ↓
6. Razorpay sends webhook to /api/webhooks/razorpay
   ↓
7. Backend updates order status to PAID
   ↓
8. User sees updated order status
```

## 🎯 API Endpoints

### Create Payment

**POST** `/api/payments/create`

Request:
```json
{
  "orderId": "order_123",
  "amount": 100000.0
}
```

Response:
```json
{
  "id": "pay_abc123",
  "orderId": "order_123",
  "amount": 100000.0,
  "status": "PENDING",
  "razorpayOrderId": "order_razorpay_xyz789",
  "createdAt": "2026-01-20T12:00:00Z"
}
```

### Webhook Endpoint

**POST** `/api/webhooks/razorpay`

Razorpay will send:
```json
{
  "event": "payment.captured",
  "payload": {
    "payment": {
      "entity": {
        "id": "pay_razorpay_123",
        "order_id": "order_razorpay_xyz789",
        "status": "captured",
        "amount": 10000000
      }
    }
  }
}
```

## 🐛 Troubleshooting

### Error: "Authentication failed"

**Cause:** Invalid or placeholder API keys

**Solution:**
1. Verify you copied the correct keys from Razorpay Dashboard
2. Make sure there are no extra spaces
3. Ensure keys start with `rzp_test_`
4. Restart the application after updating keys

### Error: "Order not found"

**Cause:** Order doesn't exist or wrong order ID

**Solution:**
1. Create order first via `/api/orders`
2. Use the exact order ID returned
3. Check order status is "CREATED"

### Webhook not receiving callbacks

**Cause:** Local development, no public URL

**Solution:**
1. Use ngrok to expose localhost
2. Configure webhook URL in Razorpay Dashboard
3. For manual testing, you can trigger webhooks via Razorpay Dashboard

## 📚 Resources

- **Razorpay Docs:** https://razorpay.com/docs/
- **API Reference:** https://razorpay.com/docs/api/
- **Test Cards:** https://razorpay.com/docs/payments/payments/test-card-details/
- **Webhooks:** https://razorpay.com/docs/webhooks/
- **Dashboard:** https://dashboard.razorpay.com/

## ✅ Verification Checklist

Before going live:
- [ ] Razorpay account created
- [ ] Test API keys configured
- [ ] Application.properties updated
- [ ] Application restarted
- [ ] Test payment successful
- [ ] Webhook configured (optional)
- [ ] Order status updates correctly

## 🎓 For Assignment/Testing

### Minimum Requirements

1. ✅ Razorpay account created
2. ✅ Test API keys configured
3. ✅ Can create Razorpay orders
4. ✅ Payment order ID returned

### Bonus Points

1. ✅ Webhook configured with ngrok
2. ✅ Payment status updates automatically
3. ✅ Complete end-to-end payment flow

---

**Your Razorpay integration is ready! 🎉**

*Use test mode keys for development and testing.*
