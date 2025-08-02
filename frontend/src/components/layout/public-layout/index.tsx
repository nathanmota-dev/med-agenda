import PublicNavbar from '../../../components/navbar';
import { Outlet } from 'react-router-dom';

export default function PublicLayout() {
    return (
        <>
            <PublicNavbar />
            <Outlet />
        </>
    );
}