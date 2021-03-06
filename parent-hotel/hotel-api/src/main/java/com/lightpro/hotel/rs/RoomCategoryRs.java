package com.lightpro.hotel.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hotel.domains.api.Room;
import com.hotel.domains.api.RoomCategory;
import com.hotel.domains.api.Rooms;
import com.infrastructure.core.PaginationSet;
import com.lightpro.hotel.cmd.RoomCategoryEdit;
import com.lightpro.hotel.cmd.RoomEdit;
import com.lightpro.hotel.vm.RoomCategoryVm;
import com.lightpro.hotel.vm.RoomVm;
import com.securities.api.Secured;

@Path("/roomCategory")
public class RoomCategoryRs extends HotelBaseRs {
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRoomCategories() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<RoomCategoryVm> roomCategories = hotel().roomCategories()
								   .all()
								   .stream()
								   .map(m -> new RoomCategoryVm(m))
								   .collect(Collectors.toList());

						return Response.ok(roomCategories).build();
					}
				});		
				
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRoomCategories(  @QueryParam("page") int page, 
										@QueryParam("pageSize") int pageSize, 
										@QueryParam("filter") String filter) throws IOException {
					
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<RoomCategoryVm> roomCategories = hotel().roomCategories()
								   .find(page, pageSize, filter)
								   .stream()
								   .map(m -> new RoomCategoryVm(m))
								   .collect(Collectors.toList());

						long count = hotel().roomCategories().count(filter);
						PaginationSet<RoomCategoryVm> pagedSet = new PaginationSet<RoomCategoryVm>(roomCategories, page, count);
						return Response.ok(pagedSet).build();
					}
				});		
				
	}
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRoomCategory(@PathParam("id") UUID id) throws IOException {

		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						RoomCategory entity = hotel().roomCategories().get(id);
						RoomCategoryVm vm = new RoomCategoryVm(entity);
						
						return Response.ok(vm).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response createRoomCategory(RoomCategoryEdit data) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						RoomCategory roomCategory = hotel().roomCategories()
								 .add(data.name(), data.capacity(), data.nightPrice());

						log.info(String.format("Cr�ation de la cat�gorie de chambre %s", roomCategory.name()));
						return Response.status(Status.CREATED)
								       .entity(new RoomCategoryVm(roomCategory))
								       .build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response editRoomCategory(@PathParam("id") UUID id, RoomCategoryEdit data) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						RoomCategory roomCategory = hotel().roomCategories().get(id);
						roomCategory.update(data.name(), data.capacity(), data.nightPrice());

						log.info(String.format("Mise � jour des donn�es de la cat�gorie de chambre %s", roomCategory.name()));
						return Response.noContent().build();
					}
				});	
		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteRoomCategory(@PathParam("id") UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						RoomCategory item = hotel().roomCategories().get(id);
						String name = item.name();
						hotel().roomCategories().delete(item);
						
						log.info(String.format("Suppression de la cat�gorie de chambre %s", name));
						return Response.noContent().build();
					}
				});
		
	}
	
	@POST
	@Secured
	@Path("/{id}/room")
	@Produces({MediaType.APPLICATION_JSON})
	public Response createRoom(@PathParam("id") UUID id, RoomEdit data) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Room room = hotel().roomCategories().get(id).rooms().add(data.number(), data.floorId());		

						log.info(String.format("Cr�ation de la chambre %s", room.number()));
						return Response.status(Status.CREATED)
								       .entity(new RoomVm(room))
								       .build();
					}
				});
		
	}
	
	@GET
	@Secured
	@Path("/{id}/room")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRooms(@PathParam("id") UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<RoomVm> roomsVm = hotel().roomCategories().get(id)
								 .rooms()
								 .all()
							     .stream()
							     .map(m -> new RoomVm(m))
							     .collect(Collectors.toList());

						return Response.ok(roomsVm).build();
					}
				});
		
	}
	
	@GET
	@Secured
	@Path("/{id}/room/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRooms(@PathParam("id") UUID id,
							 @QueryParam("page") int page, 
							 @QueryParam("pageSize") int pageSize, 
							 @QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Rooms rooms = hotel().roomCategories().get(id).rooms();
						
						List<RoomVm> roomsVm = rooms.find(page, pageSize, filter)
												    .stream()
												    .map(m -> new RoomVm(m))
												    .collect(Collectors.toList());
							
						long count = rooms.count(filter);
						PaginationSet<RoomVm> pagedSet = new PaginationSet<RoomVm>(roomsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});
				
	}
}
