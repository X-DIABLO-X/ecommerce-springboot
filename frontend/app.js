// Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Global state
let currentOrder = null;

// Product icons mapping
const productIcons = {
    'Laptop': '💻',
    'Mouse': '🖱️',
    'Keyboard': '⌨️',
    'Monitor': '🖥️',
    'Headphones': '🎧',
    'Webcam': '📷',
    'Microphone': '🎤',
    'Speaker': '🔊',
    'Phone': '📱',
    'Tablet': '📱'
};

// Sample Products Data
const sampleProducts = [
    {
        name: 'Gaming Laptop',
        description: 'High-performance gaming laptop with RTX 4070',
        price: 125000.0,
        stock: 15
    },
    {
        name: 'Wireless Mouse',
        description: 'Ergonomic wireless mouse with RGB lighting',
        price: 2500.0,
        stock: 50
    },
    {
        name: 'Mechanical Keyboard',
        description: 'RGB mechanical keyboard with blue switches',
        price: 8500.0,
        stock: 30
    },
    {
        name: '4K Monitor',
        description: '27-inch 4K UHD monitor with HDR support',
        price: 35000.0,
        stock: 20
    },
    {
        name: 'Gaming Headphones',
        description: '7.1 surround sound gaming headset',
        price: 5500.0,
        stock: 40
    },
    {
        name: 'HD Webcam',
        description: '1080p webcam with auto-focus',
        price: 4500.0,
        stock: 25
    }
];

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    loadUserData();
});

// Navigation
function showSection(sectionName) {
    // Update nav buttons
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.closest('.nav-btn').classList.add('active');

    // Update sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(`${sectionName}-section`).classList.add('active');

    // Load data for the section
    if (sectionName === 'cart') {
        loadCart();
    } else if (sectionName === 'orders') {
        loadOrders();
    }
}

// Show toast notification
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Get User ID
function getUserId() {
    return document.getElementById('userId').value || 'user123';
}

// Load user-specific data
function loadUserData() {
    loadCart();
    loadOrders();
}

// ==================== PRODUCTS ====================

async function loadProducts() {
    const grid = document.getElementById('products-grid');
    grid.innerHTML = '<div class="loading"><i class="fas fa-spinner"></i> Loading products...</div>';

    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        const products = await response.json();

        if (products.length === 0) {
            grid.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-box-open"></i>
                    <p>No products available. Click "Load Sample Products" to get started!</p>
                </div>
            `;
            return;
        }

        grid.innerHTML = products.map(product => createProductCard(product)).join('');
    } catch (error) {
        console.error('Error loading products:', error);
        grid.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Error loading products. Make sure the backend is running!</p>
            </div>
        `;
    }
}

function createProductCard(product) {
    const icon = getProductIcon(product.name);
    const stockClass = product.stock < 10 ? 'low-stock' : '';
    
    return `
        <div class="product-card">
            <div class="product-icon">${icon}</div>
            <div class="product-name">${product.name}</div>
            <div class="product-description">${product.description || 'No description'}</div>
            <div class="product-price">₹${product.price.toLocaleString('en-IN')}</div>
            <div class="product-stock ${stockClass}">
                <i class="fas fa-box"></i> Stock: ${product.stock}
            </div>
            <div class="product-actions">
                <input type="number" class="quantity-input" id="qty-${product.id}" value="1" min="1" max="${product.stock}">
                <button class="btn btn-primary" onclick="addToCart('${product.id}')" style="flex: 1">
                    <i class="fas fa-cart-plus"></i> Add to Cart
                </button>
            </div>
        </div>
    `;
}

function getProductIcon(productName) {
    for (let key in productIcons) {
        if (productName.toLowerCase().includes(key.toLowerCase())) {
            return productIcons[key];
        }
    }
    return '📦';
}

async function initializeSampleProducts() {
    const btn = event.target;
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Creating products...';

    try {
        let created = 0;
        for (const product of sampleProducts) {
            const response = await fetch(`${API_BASE_URL}/products`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            });
            if (response.ok) created++;
        }

        showToast(`${created} sample products created successfully!`, 'success');
        loadProducts();
    } catch (error) {
        console.error('Error creating products:', error);
        showToast('Error creating products', 'error');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i class="fas fa-database"></i> Load Sample Products';
    }
}

// ==================== CART ====================

async function addToCart(productId) {
    const quantityInput = document.getElementById(`qty-${productId}`);
    const quantity = parseInt(quantityInput.value) || 1;
    const userId = getUserId();

    try {
        const response = await fetch(`${API_BASE_URL}/cart/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId,
                productId,
                quantity
            })
        });

        if (response.ok) {
            showToast('Product added to cart!', 'success');
            updateCartCount();
        } else {
            const error = await response.json();
            showToast(error.message || 'Error adding to cart', 'error');
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        showToast('Error adding to cart', 'error');
    }
}

async function loadCart() {
    const container = document.getElementById('cart-items');
    const userId = getUserId();
    
    container.innerHTML = '<div class="loading"><i class="fas fa-spinner"></i> Loading cart...</div>';

    try {
        const response = await fetch(`${API_BASE_URL}/cart/${userId}`);
        const cartItems = await response.json();

        if (cartItems.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-shopping-cart"></i>
                    <p>Your cart is empty</p>
                </div>
            `;
            updateCartSummary([]);
            return;
        }

        container.innerHTML = cartItems.map(item => createCartItemHTML(item)).join('');
        updateCartSummary(cartItems);
        updateCartCount();
    } catch (error) {
        console.error('Error loading cart:', error);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Error loading cart</p>
            </div>
        `;
    }
}

function createCartItemHTML(item) {
    const product = item.product;
    const icon = getProductIcon(product.name);
    const itemTotal = product.price * item.quantity;
    
    return `
        <div class="cart-item">
            <div class="cart-item-icon">${icon}</div>
            <div class="cart-item-details">
                <div class="cart-item-name">${product.name}</div>
                <div class="cart-item-price">₹${product.price.toLocaleString('en-IN')} each</div>
                <div class="cart-item-quantity">Quantity: ${item.quantity}</div>
            </div>
            <div class="cart-item-total">₹${itemTotal.toLocaleString('en-IN')}</div>
        </div>
    `;
}

function updateCartSummary(cartItems) {
    const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalAmount = cartItems.reduce((sum, item) => 
        sum + (item.product.price * item.quantity), 0);

    document.getElementById('total-items').textContent = totalItems;
    document.getElementById('total-amount').textContent = 
        `₹${totalAmount.toLocaleString('en-IN')}`;
}

async function updateCartCount() {
    const userId = getUserId();
    
    try {
        const response = await fetch(`${API_BASE_URL}/cart/${userId}`);
        const cartItems = await response.json();
        const count = cartItems.reduce((sum, item) => sum + item.quantity, 0);
        
        const badge = document.getElementById('cart-count');
        badge.textContent = count;
        badge.style.display = count > 0 ? 'flex' : 'none';
    } catch (error) {
        console.error('Error updating cart count:', error);
    }
}

async function clearCart() {
    if (!confirm('Are you sure you want to clear your cart?')) return;

    const userId = getUserId();

    try {
        const response = await fetch(`${API_BASE_URL}/cart/${userId}/clear`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast('Cart cleared successfully', 'success');
            loadCart();
        }
    } catch (error) {
        console.error('Error clearing cart:', error);
        showToast('Error clearing cart', 'error');
    }
}

// ==================== ORDERS ====================

async function createOrder() {
    const userId = getUserId();

    try {
        const response = await fetch(`${API_BASE_URL}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId })
        });

        if (response.ok) {
            const order = await response.json();
            currentOrder = order;
            showToast('Order created successfully!', 'success');
            loadCart();
            
            // Open payment modal
            openPaymentModal(order);
        } else {
            const error = await response.json();
            showToast(error.message || 'Error creating order', 'error');
        }
    } catch (error) {
        console.error('Error creating order:', error);
        showToast('Error creating order', 'error');
    }
}

async function loadOrders() {
    const container = document.getElementById('orders-list');
    const userId = getUserId();
    
    container.innerHTML = '<div class="loading"><i class="fas fa-spinner"></i> Loading orders...</div>';

    try {
        const response = await fetch(`${API_BASE_URL}/orders/user/${userId}`);
        const orders = await response.json();

        if (orders.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-receipt"></i>
                    <p>No orders yet</p>
                </div>
            `;
            return;
        }

        container.innerHTML = orders.map(order => createOrderCardHTML(order)).join('');
    } catch (error) {
        console.error('Error loading orders:', error);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Error loading orders</p>
            </div>
        `;
    }
}

function createOrderCardHTML(order) {
    const date = new Date(order.createdAt).toLocaleString('en-IN');
    
    return `
        <div class="order-card">
            <div class="order-header">
                <div>
                    <div class="order-id"><i class="fas fa-hashtag"></i> ${order.id.substring(0, 8)}...</div>
                    <div style="color: var(--text-secondary); font-size: 0.875rem;">
                        <i class="fas fa-clock"></i> ${date}
                    </div>
                </div>
                <div class="order-status ${order.status}">${order.status}</div>
            </div>
            <div class="order-items">
                ${order.items.map(item => `
                    <div class="order-item">
                        <span>${getProductIcon(item.productName)} ${item.productName} x ${item.quantity}</span>
                        <span>₹${(item.price * item.quantity).toLocaleString('en-IN')}</span>
                    </div>
                `).join('')}
            </div>
            <div class="order-total">
                Total: ₹${order.totalAmount.toLocaleString('en-IN')}
            </div>
            ${order.status === 'CREATED' ? `
                <button class="btn btn-primary btn-large" onclick="retryPayment('${order.id}', ${order.totalAmount})">
                    <i class="fas fa-credit-card"></i> Pay Now
                </button>
            ` : ''}
        </div>
    `;
}

// ==================== PAYMENT ====================

function openPaymentModal(order) {
    document.getElementById('payment-order-id').textContent = order.id;
    document.getElementById('payment-amount').textContent = 
        `₹${order.totalAmount.toLocaleString('en-IN')}`;
    document.getElementById('payment-status').innerHTML = '';
    document.getElementById('payment-modal').classList.add('active');
}

function closePaymentModal() {
    document.getElementById('payment-modal').classList.remove('active');
    loadOrders();
}

function retryPayment(orderId, amount) {
    currentOrder = { id: orderId, totalAmount: amount };
    openPaymentModal(currentOrder);
}

async function processPayment(paymentMode) {
    const statusDiv = document.getElementById('payment-status');
    statusDiv.className = 'payment-status processing';
    statusDiv.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing payment...';

    const buttons = document.querySelectorAll('.payment-options button');
    buttons.forEach(btn => btn.disabled = true);

    try {
        const response = await fetch(`${API_BASE_URL}/payments/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                orderId: currentOrder.id,
                amount: currentOrder.totalAmount,
                paymentMode
            })
        });

        if (response.ok) {
            const payment = await response.json();
            
            if (paymentMode === 'MOCK') {
                statusDiv.className = 'payment-status processing';
                statusDiv.innerHTML = `
                    <i class="fas fa-clock"></i> Payment initiated! 
                    Waiting for webhook callback (~3 seconds)...
                `;
                
                // Poll for order status
                setTimeout(() => pollOrderStatus(currentOrder.id), 3500);
            } else {
                statusDiv.className = 'payment-status success';
                statusDiv.innerHTML = `
                    <i class="fas fa-check-circle"></i> Payment initiated!<br>
                    <small>Razorpay Order ID: ${payment.razorpayOrderId}</small><br>
                    <small>Note: In production, user would be redirected to Razorpay payment page</small>
                `;
            }
        } else {
            const error = await response.json();
            statusDiv.className = 'payment-status error';
            statusDiv.innerHTML = `<i class="fas fa-times-circle"></i> ${error.message || 'Payment failed'}`;
        }
    } catch (error) {
        console.error('Error processing payment:', error);
        statusDiv.className = 'payment-status error';
        statusDiv.innerHTML = '<i class="fas fa-times-circle"></i> Payment processing error';
    } finally {
        buttons.forEach(btn => btn.disabled = false);
    }
}

async function pollOrderStatus(orderId) {
    const statusDiv = document.getElementById('payment-status');
    
    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}`);
        const order = await response.json();
        
        if (order.status === 'PAID') {
            statusDiv.className = 'payment-status success';
            statusDiv.innerHTML = `
                <i class="fas fa-check-circle"></i> Payment successful! 
                Order status updated to PAID.
            `;
            showToast('Payment completed successfully!', 'success');
            setTimeout(closePaymentModal, 2000);
        } else if (order.status === 'FAILED') {
            statusDiv.className = 'payment-status error';
            statusDiv.innerHTML = '<i class="fas fa-times-circle"></i> Payment failed';
        } else {
            statusDiv.innerHTML += '<br><small>Still waiting...</small>';
            setTimeout(() => pollOrderStatus(orderId), 2000);
        }
    } catch (error) {
        console.error('Error polling order status:', error);
    }
}
