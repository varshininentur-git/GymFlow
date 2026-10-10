const indianRupeeFormatter = new Intl.NumberFormat('en-IN', {
  style: 'currency',
  currency: 'INR',
});

export const formatCurrency = amount => indianRupeeFormatter.format(Number(amount ?? 0));
