const API_URL = 'http://localhost:5000/api/users';

export const getUserByUsername = async (username) => {
  const res = await fetch(`${API_URL}/${username}`);
  if (!res.ok) throw new Error('User not found');
  return res.json();
};

export const updateUser = async (id, data) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });
  if (!res.ok) throw new Error('Update failed');
  return res.json();
};

export const registerUser = async (userData) => {
  const res = await fetch(`${API_URL}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });
  if (!res.ok) throw new Error((await res.json()).message || 'Register failed');
  return res.json();
}; 