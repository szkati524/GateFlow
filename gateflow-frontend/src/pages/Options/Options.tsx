import { useState, useEffect } from "react";
import Navbar from "../../components/Common/Navbar";
import styles from './Options.module.css'; 
import { apiFetch } from "../../api";

interface User {
    id: number;
    username: string;
    fullName: string; 
    role: string;
}

const OptionsPage = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false); 
    const [users, setUsers] = useState<User[]>([]);

    const [newUser, setNewUser] = useState({ username: '', fullName: '', password: '', role: 'ROLE_SECURITY' });
    const [pwdData, setPwdData] = useState({ old: '', new1: '', new2: '' });

    const [editingUser, setEditingUser] = useState<{
        id: number;
        username: string;
        fullName: string;
        role: string;
        password?: string;
    } | null>(null);

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            try {
                const base64Url = token.split('.')[1];
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(c => 
                    '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
                ).join(''));

                const decoded = JSON.parse(jsonPayload);
                const username = decoded.sub;
                
                if (username === 'admin' || decoded.role === 'ADMIN' || decoded.role === 'ROLE_ADMIN') {
                    setIsAdmin(true);
                    fetchUsers();
                }
            } catch (err) {
                console.error("Błąd dekodowania tokena:", err);
            }
        }
    }, []);
        
    const fetchUsers = async () => {
        try {
            const res = await apiFetch('/api/users');
            if (res.ok) {
                const data = await res.json();
                setUsers(data);
            }
        } catch (err) {
            console.error("Błąd pobierania listy użytkowników:", err);
        }
    };

    const handleCreateUser = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await apiFetch('/api/users', {
                method: 'POST',
                body: JSON.stringify(newUser)
            });

            if (res.ok) {
                alert("Użytkownik utworzony pomyślnie!");
                setNewUser({ username: '', fullName: '', password: '', role: 'ROLE_SECURITY' });
                fetchUsers();
            } else {
                const errorData = await res.json().catch(() => ({}));
                alert(`Błąd: ${errorData.message || "Brak uprawnień lub błąd danych"}`);
            }
        } catch (err) {
            alert("Błąd połączenia z serwerem.");
        }
    };

    const handleDeleteUser = async (id: number) => {
        if (!window.confirm("Czy na pewno usunąć użytkownika?")) return;
        try {
            const res = await apiFetch(`/api/users/${id}`, { method: 'DELETE' });
            if (res.ok) {
                if (editingUser?.id === id) setEditingUser(null);
                fetchUsers();
            }
        } catch (err) {
            console.error(err);
        }
    };

    const handleChangePassword = async (e: React.FormEvent) => {
        e.preventDefault();

        if (pwdData.new1 !== pwdData.new2) {
            alert("Nowe hasła nie są identyczne!");
            return;
        }

        const token = localStorage.getItem("token");

        try {
            const res = await apiFetch('/api/users/change-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    oldPassword: pwdData.old,
                    newPassword: pwdData.new1
                })
            });

            if (res.ok) {
                alert("Hasło zostało pomyślnie zmienione!");
                setPwdData({ old: '', new1: '', new2: '' }); 
            } else {
                const errorData = await res.json().catch(() => ({}));
                alert(`Błąd: ${errorData.message || "Błąd autoryzacji lub niepoprawne stare hasło"}`);
            }
        } catch (err) {
            alert("Błąd połączenia z serwerem.");
        }
    };

   
    const handleStartEdit = (user: User) => {
        setEditingUser({
            id: user.id,
            username: user.username,
            fullName: user.fullName,
            role: user.role,
            password: ''
        });
    };

   
    const handleUpdateUser = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!editingUser) return;

        try {
            const res = await apiFetch(`/api/users/${editingUser.id}`, {
                method: 'PUT',
                body: JSON.stringify(editingUser)
            });

            if (res.ok) {
                alert("Dane użytkownika zostały zaktualizowane!");
                setEditingUser(null);
                fetchUsers();
            } else {
                const errorData = await res.json().catch(() => ({}));
                alert(`Błąd: ${errorData.message || "Nie udało się zaktualizować użytkownika"}`);
            }
        } catch (err) {
            alert("Błąd połączenia z serwerem.");
        }
    };

    return (
        <div className={styles.wrapper}>
            <Navbar isSidebarOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.listsContainer}>
                    <section className={styles.listSection}>
                        <div className={styles.sectionHeader}>
                            <h2>Zmiana Twojego hasła</h2>
                        </div>
                        <form className={styles.passwordForm} onSubmit={handleChangePassword}>
                            <input 
                                type="password" 
                                placeholder="Stare hasło" 
                                className={styles.inputField} 
                                value={pwdData.old}
                                onChange={(e) => setPwdData({...pwdData, old: e.target.value})}
                                required
                            />
                            <input 
                                type="password" 
                                placeholder="Nowe hasło" 
                                className={styles.inputField} 
                                value={pwdData.new1}
                                onChange={(e) => setPwdData({...pwdData, new1: e.target.value})}
                                required
                            />
                            <input 
                                type="password" 
                                placeholder="Powtórz nowe" 
                                className={styles.inputField} 
                                value={pwdData.new2}
                                onChange={(e) => setPwdData({...pwdData, new2: e.target.value})}
                                required
                            />
                            <button type="submit" className={styles.whiteActionBtn}>Zmień hasło</button>
                        </form>
                    </section>

                    {isAdmin && (
                        <section className={styles.listSection}>
                            <div className={styles.sectionHeader}>
                                <h2>Nowy Użytkownik</h2>
                            </div>
                            <form className={styles.adminInlineForm} onSubmit={handleCreateUser}>
                                <input 
                                    placeholder="Imię i Nazwisko" 
                                    className={styles.inputField} 
                                    value={newUser.fullName}
                                    onChange={(e) => setNewUser({...newUser, fullName: e.target.value})}
                                    required
                                />
                                <input 
                                    placeholder="Login" 
                                    className={styles.inputField} 
                                    value={newUser.username}
                                    onChange={(e) => setNewUser({...newUser, username: e.target.value})}
                                    required
                                />
                                <input 
                                    type="password" 
                                    placeholder="Hasło" 
                                    className={styles.inputField} 
                                    value={newUser.password}
                                    onChange={(e) => setNewUser({...newUser, password: e.target.value})}
                                    required
                                />
                                <select 
                                    className={styles.inputField}
                                    value={newUser.role}
                                    onChange={(e) => setNewUser({...newUser, role: e.target.value})}
                                >
                                    <option value="ROLE_SECURITY">SECURITY</option>
                                    <option value="ROLE_ADMIN">ADMIN</option>
                                </select>
                                <button type="submit" className={styles.whiteActionBtn}>Utwórz konto</button>
                            </form>
                        </section>
                    )}

                    {isAdmin && (
                        <section className={styles.listSection}>
                            <div className={styles.sectionHeader}>
                                <h2>Zarządzaj użytkownikami</h2>
                            </div>
                            <table className={styles.vehicleTable}>
                                <thead>
                                    <tr>
                                        <th>Imię i Nazwisko</th>
                                        <th>Login</th>
                                        <th>Rola</th>
                                        <th>Akcje</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {users.length > 0 ? users.map(u => (
                                        <tr key={u.id}>
                                            <td>{u.fullName}</td>
                                            <td>{u.username}</td>
                                            <td><strong>{u.role}</strong></td>
                                            <td className={styles.tableActions}>
                                                <button 
                                                    className={styles.editLink} 
                                                    onClick={() => handleStartEdit(u)}
                                                >
                                                    Edytuj
                                                </button>
                                                <button 
                                                    className={styles.deleteLink} 
                                                    onClick={() => handleDeleteUser(u.id)}
                                                >
                                                    Usuń
                                                </button>
                                            </td>
                                        </tr>
                                    )) : (
                                        <tr><td colSpan={4} style={{textAlign: 'center', padding: '20px'}}>Brak użytkowników do wyświetlenia</td></tr>
                                    )}
                                </tbody>
                            </table>

                            
                            {editingUser && (
                                <div style={{ marginTop: '25px', paddingTop: '20px', borderTop: '1px solid #333' }}>
                                    <h3>Edycja użytkownika: <span style={{ color: 'orange' }}>{editingUser.username}</span></h3>
                                    <form className={styles.adminInlineForm} onSubmit={handleUpdateUser}>
                                        <input 
                                            placeholder="Imię i Nazwisko" 
                                            className={styles.inputField} 
                                            value={editingUser.fullName}
                                            onChange={(e) => setEditingUser({...editingUser, fullName: e.target.value})}
                                            required
                                        />
                                        <input 
                                            placeholder="Login" 
                                            className={styles.inputField} 
                                            value={editingUser.username}
                                            onChange={(e) => setEditingUser({...editingUser, username: e.target.value})}
                                            required
                                        />
                                        <input 
                                            type="password" 
                                            placeholder="Nowe hasło (opcjonalnie)" 
                                            className={styles.inputField} 
                                            value={editingUser.password || ''}
                                            onChange={(e) => setEditingUser({...editingUser, password: e.target.value})}
                                        />
                                        <select 
                                            className={styles.inputField}
                                            value={editingUser.role}
                                            onChange={(e) => setEditingUser({...editingUser, role: e.target.value})}
                                        >
                                            <option value="ROLE_SECURITY">SECURITY</option>
                                            <option value="ROLE_ADMIN">ADMIN</option>
                                        </select>
                                        <div style={{ display: 'flex', gap: '10px', gridColumn: 'span 2' }}>
                                            <button type="submit" className={styles.whiteActionBtn}>Zapisz zmiany</button>
                                            <button 
                                                type="button" 
                                                className={styles.whiteActionBtn} 
                                                style={{ backgroundColor: '#444' }}
                                                onClick={() => setEditingUser(null)}
                                            >
                                                Anuluj
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            )}
                        </section>
                    )}
                </div>
            </main>
        </div>
    );
};

export default OptionsPage;