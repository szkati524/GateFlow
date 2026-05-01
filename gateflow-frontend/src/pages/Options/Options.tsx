import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Common/Header";
import styles from './Options.module.css'; 
import { apiFetch } from "../../api";

interface User {
    id: number;
    username: string;
    fullName: string; 
    role: string;
}

const OptionsPage = () => {
    const navigate = useNavigate();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false); 
    const [users, setUsers] = useState<User[]>([]);

    const [newUser, setNewUser] = useState({ username: '', fullName: '', password: '', role: 'ROLE_SECURITY' });
    const [pwdData, setPwdData] = useState({ old: '', new1: '', new2: '' });

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            try {
                const base64Url = token.split('.')[1];
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                }).join(''));

                const decoded = JSON.parse(jsonPayload);
                const username = decoded.sub;
                
                // Sprawdzanie czy użytkownik ma uprawnienia admina
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
            if (res.ok) fetchUsers();
        } catch (err) {
            console.error(err);
        }
    };

    // POPRAWIONA FUNKCJA: Zmiana hasła z dołączonym tokenem
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
                    'Authorization': `Bearer ${token}` // <--- Dodanie autoryzacji rozwiązuje błąd 403
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
                // 403 może też oznaczać błędne stare hasło, jeśli backend tak to zwraca
                alert(`Błąd: ${errorData.message || "Błąd autoryzacji lub niepoprawne stare hasło"}`);
            }
        } catch (err) {
            alert("Błąd połączenia z serwerem.");
        }
    };

    return (
        <div className={styles.wrapper}>
            <Header />
            
            <button className={styles.menuTrigger} onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
                {isSidebarOpen ? '✕' : '☰'}
            </button>

            <div className={styles.rightActions}>
                <button className={styles.iconBtn} onClick={() => navigate('/options')}>⚙️</button>
                <button className={styles.whiteLogoutBtn} onClick={() => { localStorage.clear(); navigate('/login'); }}>Wyloguj</button>
            </div>

            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a onClick={() => navigate('/')}>Ruch pojazdów</a>
                    <a onClick={() => navigate('/add-entry')}>Dodaj wjazd</a>
                    <a onClick={() => navigate('/search')}>Wyszukaj</a>
                    <a onClick={() => navigate('/raport')}>Raporty</a>
                </nav>
            </aside>

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
                                                <button className={styles.editLink}>Edytuj</button>
                                                <button className={styles.deleteLink} onClick={() => handleDeleteUser(u.id)}>Usuń</button>
                                            </td>
                                        </tr>
                                    )) : (
                                        <tr><td colSpan={4} style={{textAlign: 'center', padding: '20px'}}>Brak użytkowników do wyświetlenia</td></tr>
                                    )}
                                </tbody>
                            </table>
                        </section>
                    )}
                </div>
            </main>
        </div>
    );
};

export default OptionsPage;