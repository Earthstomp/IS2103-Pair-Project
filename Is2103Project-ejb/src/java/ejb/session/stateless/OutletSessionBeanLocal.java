/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Keith test
 */
@Local
public interface OutletSessionBeanLocal {

    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;

    public List<Outlet> retrieveAllOutlets();

    public Long createNewOutlet(Outlet outlet) throws OutletExistsException, UnknownPersistenceException;
    
    public Outlet retrieveOutletByLocation(String location) throws OutletNotFoundException;
    
}
