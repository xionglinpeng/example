package com.redis.example.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Pipeline implements ApplicationRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static final String[] ids = new String[]{
            "38fcda29-1307-4148-932c-8ef42c13df7d","f89c015f-d41f-4840-b6ec-0b0a5c0b4ef5","88ba6214-b768-4ddb-8faf-185eed718444","a208b422-8ac3-48e0-9196-2ccd49a09d3b","9dfd9633-5b34-4244-904b-b5a07e864ae9",
            "3dd7eac3-4d41-4e3a-ad1c-92e8717de4dd","1958e041-9f62-4041-9284-bd11b9ba6211","dccc4ded-879e-46ae-852b-d42eca6fa79a","0903ad2c-3d68-42e0-8d8d-79f216ba131e","73fc46e8-f0cd-499a-9383-744b1392e75b",
            "0fa64e4c-d597-48c5-b918-72ed491c0c5d","0776ae0b-cc38-42ca-90cd-84d4c04d2dcd","bff55cca-5570-4875-811c-1f75291e00c5","30b1ed44-eaf9-42c0-becc-fdf2a70be9b5","8d6099bf-692f-452f-b0f0-244d926a4f2b",
            "53be1bc6-8a50-4039-8351-cfd010bb5548","17094775-ab37-49d7-8963-c39be5ef2bc2","9d2ae0cf-eac2-440b-a962-1ff6b5121469","3b211d4c-2df8-4fb8-99f2-9519d9cd814e","e795b40e-24d9-4a1b-81bb-a1cbd24fadfb",
            "5e602dcd-e922-4f22-be11-844819cdde00","3ab3411c-50e7-43b9-84dd-adb6c5063083","533a19a1-de5c-4ea1-b482-ad5d91fc6a46","88f8f3be-975e-4b84-9c3a-a262e036165f","c5547aae-f03b-4462-a9c3-0117bd4eeb87",
            "26f08368-06d1-4714-af86-2cdbb91daa88","f8d1cd8e-16e5-4b3b-826a-b9dab3921634","38b61a65-37d5-494c-ae10-e486ef197346","96574715-cb03-4d94-85f4-3d2496931da1","be001ddf-8ede-4906-9839-ff9b1d938951",
            "01b10875-81f8-4ad6-bdf0-91afe0146037","ab109306-570e-4b7f-b716-6ab4285029a3","835481ec-7d96-4a35-8991-a4dc76df8127","90fa365d-0d42-4c17-9ec1-d7b7490ef352","0ddc6d43-97e3-4c3c-adb6-d72d506b4d6a",
            "1de6f6f2-acbf-4686-9b9a-0d0f9733ab8e","cc8cc2ed-007f-42be-94fa-a46cdcd49e3e","36e56392-3380-45eb-bff9-99d76d664e19","f022900e-17f0-461e-951a-55189e8b1bfe","192c526d-800c-43df-b985-169162a453d3",
            "36a1f348-2a57-4c61-b60e-fe73ebf40a48","61a24d95-1aec-467f-a5cc-9d296dfdd422","4aaa55a8-17ed-4e2e-a900-01e7695c5780","0866e174-208f-4d65-bfb8-0c7149805576","dbd1b9a5-8230-4376-b9e3-89e697c09d72",
            "affd549a-0269-43fb-a42f-7f00de896583","a53b9ef0-1179-4a51-91d9-cdbae36bd3b0","8521cf1e-3ac0-45e4-b784-dcd938ad2860","665b7d78-98b3-4f9c-bfc1-1373b3154dde","faa99cd9-6355-4e92-b5cc-77feeb1125ff",
            "ffc18a20-6b5b-4f7a-ba98-36ac16a13669","643b0350-a495-4811-92c9-4812d99af465","9a78cd26-483e-485b-9bae-2daed141cf95","78c89b7e-8cf4-42aa-ba72-c5ffa7aa924c","0c17786a-322a-4c1e-9b62-6cc965f8a7df",
            "886a9c19-e548-4ceb-8f2f-5deeba30039a","3417fc41-3b2f-4c2d-8e89-11707cbfc68a","cc3b7de0-2f7a-44ef-80e5-24f5e70d3fe4","e24ce0bf-2109-47e0-942b-ad2df3e5597a","c5d649b2-ab99-45ea-9235-9b1cf72d8113",
            "c2ef6bfb-2fe3-4831-8b0d-747dd9f6fd2d","589d673d-8915-40ab-a8ff-91a71ec7e4fd","cfe40892-ff1f-4e6a-a0f6-dd82d4569600","dedb5ca3-bd5b-4353-891a-c1dd4d31550d","77c1912e-0111-4946-8a33-337922ccbab1",
            "51a93023-63fa-4851-9b76-e8950746d78d","c29e861a-a3a1-4fb9-b381-07a46144ecda","9062cbf3-dd64-4622-a8f3-04155929a390","87ab210d-05cc-453c-8929-1e7bfe71d4ab","ef7b2d50-71ea-44b2-a462-55ff88da56fa",
            "11ef2f96-170f-4890-a80c-0562542797d8","e7cee10a-9da2-45a2-9af7-b03579c2a23b","a75037b0-ebd6-40a5-818a-dc97586102a1","e5d2d25e-3614-45b6-b35b-332ac3c2ff68","0df5e608-fd68-499d-9a21-3906f7969611",
            "4594ec6b-0eca-4353-9443-0257efa93999","bdae909c-a0f2-4125-ba92-83ae23e4e647","e12c1636-2e7d-4930-8e0e-681e0264d2ef","70cf13f2-5467-481a-af27-05c209def1b8","db453ab3-e4cc-4824-b705-bddf5de92a65",
            "5205190e-1ed8-4ad8-a947-879d70c1c7a4","caf828fc-000e-44ff-a435-f2d1ea40b265","a5262896-997b-463a-8e53-063c5a029248","bd97e527-bee8-4bc8-a0f7-4d94617f23f6","a74c5d0b-4634-4930-ba01-aa30fda1a6c3",
            "b3741a47-d971-41e0-8d65-696e5f41303d","b8a323ae-07ab-42b5-92dd-be16e720bcc1","99c1bfd8-c627-4410-8c17-3c5416a3d5c0","13d55d13-ed02-427e-b3da-4bd6282a87e2","e35286d5-09e1-40f2-a7b0-9f1274f3e2ed",
            "c489aff6-2806-4b81-808b-0297e3d77359","6e004963-1ce6-4586-8d8d-31d365aa7b8c","50d6dfbb-d169-49f4-98e2-4b15636d13f0","83f14365-a5e2-4be0-8a9a-441d996f6392","76092ab8-272c-46ab-bf16-ff8568697cff",
            "97f05b3a-7613-45a5-ab92-3b775788d746","0d68c6b3-4b8a-4fe0-a0f9-23f7e46b6ede","59a12466-9e08-4afb-a3e4-95d610a0eb7b","4ab5d98f-baf4-4b1f-85be-8e03c2191635","17ffbc4c-4120-4b51-a601-599025068be1"};

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        Random random = new Random();
        for (String id : ids) {
//            operations.set(id, random.nextInt()+"");
            hashOperations.put("{hash}"+id,id,random.nextInt()+"");
        }



        useExecutePipelined();
        useExecutePipelined();
        notUsePipelined();
        usePipelined();
        useExecutePipelined1();
    }

    /**
     * 使用 {@link RedisTemplate#execute(RedisCallback)} 执行pipeline.
     */
    public void useExecutePipelined(){
        long startTime = System.currentTimeMillis();
        List<Object> objects = new ArrayList<>(100);
        Object execute = stringRedisTemplate.execute(connection -> {
            connection.openPipeline();
            for (String id : ids) {
                connection.get(stringRedisSerializer.serialize(id));
            }
            List<Object> objs = connection.closePipeline();
            objs.forEach(o -> objects.add(genericJackson2JsonRedisSerializer.deserialize((byte[]) o)));
            return null;
        },false,true);
        System.out.println("use execute pipelined : "+(System.currentTimeMillis() - startTime)+" ms");
    }

    /**
     * 不使用pipeline。
     */
    public void notUsePipelined(){
        long startTime = System.currentTimeMillis();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        List<Object> objects = new ArrayList<>(100);
        for (String id : ids) {
            String user = operations.get(id);
            objects.add(genericJackson2JsonRedisSerializer.deserialize(user.getBytes(StandardCharsets.UTF_8)));
        }
        System.out.println("not use pipelined : "+(System.currentTimeMillis() - startTime)+" ms");
    }

    /**
     * 使用 {@link RedisTemplate#executePipelined(RedisCallback)} 执行pipeline.
     */
    public void usePipelined(){
        long startTime = System.currentTimeMillis();
        List<Object> objects = stringRedisTemplate.executePipelined((RedisCallback<?>) connection -> {
            for (String id : ids)
                connection.hGet(stringRedisSerializer.serialize("{hash}"+id),stringRedisSerializer.serialize(id));
//                connection.get(stringRedisSerializer.serialize(id));
            return null;
        });
        System.out.println(" =  "+objects);
        System.out.println("use pipelined : "+
                (System.currentTimeMillis() - startTime)+" ms");
    }

//    use execute pipelined : 1200 ms
//    use execute pipelined : 19 ms
//    not use pipelined : 129 ms
//    use pipelined : 13 ms

    /**
     * @see RedisTemplate#execute(RedisCallback, boolean, boolean)
     */
    public void useExecutePipelined1(){
        Object execute = stringRedisTemplate.execute(connection -> {
            for (String id : ids) {
                connection.get(stringRedisSerializer.serialize(id));
            }
            return null;
        },false,true);
        System.out.println("use execute pipelined : "+execute);
    }
}
