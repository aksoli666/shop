DELETE FROM books_categories WHERE category_id IN (SELECT id FROM categories WHERE name = 'E Category');
DELETE FROM categories WHERE name = 'E Category';
