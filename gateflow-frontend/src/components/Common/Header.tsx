const Header = () => {
    return (
        <header style ={{
            width: '100%',
            textAlign:'center',
            padding: '16px 50px',
            backgroundColor: 'orange',
            borderBottom: '2px solid #e68e68',
            color: '#fff',
            fontSize: '36px',
            fontWeight: 'bold',
            position: 'fixed',
            boxSizing: 'border-box',
            top: 0,
            left:0,
            zIndex:1000
        }}>
            GateFlow
        </header>
    );
};
export default Header;