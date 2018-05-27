package com.client.handlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.client.ClientManager;


public class ResponseHandlerDownload extends ResponseHandler
{
	private String message;
	private String path;

	public ResponseHandlerDownload(String status, String message)
	{
		super("RES_DOWNLOAD", status);
		this.message = message;
	}

	@Override
	public void run()
	{
		String decodedMessage = new String(Base64.getDecoder().decode(this.message));
		
		String[] split = decodedMessage.split(" ");
		
		if(status == 0) {
			System.out.println(decodedMessage);
			ClientManager.getInstance().log(decodedMessage);
			return;
		}
		
		String title = new String(Base64.getDecoder().decode(new String(Base64.getDecoder().decode(split[0]),StandardCharsets.US_ASCII)));

		byte[] data = Base64.getDecoder().decode(split[1]);
		
		int onSave = this.save(title, data);
		
		String res = ""; 
		
		switch (onSave) {
		case 0:
			res = "Track " + title + " successfully downloaded, now playing...";
			break;
		case -1:
			res = "File " + title + " has already been downloaded, now playing...";
			break;
		default:
			res = "There was an error processing the track " + title;
			break;
		}
		
		ClientManager.getInstance().log(res);
		
		if(onSave != 0) 
			play(title);
	}

	public int save(String title, byte[] data)
	{
        String tempDir = System.getProperty("java.io.tmpdir");
		this.path = tempDir + "/" + title;
		
		try
		{		
			File file = new File(path);
			
			if(file.exists())
				return -1;
			
			if (data != null) 
			{
				DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
				stream.write(data, 0, data.length);
				stream.close();
			}
			else
				return 0;
			
			return 1;
		} 
		catch (IOException e)
		{
			System.out.println("Error while writting to " + path);
			return 0;
		}
	}

	private void play(String title) 
	{
		try{
			Music track = Gdx.audio.newMusic(Gdx.files.internal(this.path));
			ClientManager.getInstance().setActiveTrack(track);
			track.play();
			track.setOnCompletionListener(new Music.OnCompletionListener() {

				@Override
				public void onCompletion(Music track) {
					track.stop();
					ClientManager.getInstance().log(title + " has finished playing");
					track.dispose();
				}

			});
			
		} catch(GdxRuntimeException e) {
			ClientManager.getInstance().log("The track " + title + " can't be played. Maybe it's not in the right format!");
		}
	}

}
